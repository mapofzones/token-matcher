package com.mapofzones.tokenmatcher.config;

import com.mapofzones.tokenmatcher.common.properties.PathfinderProperties;
import com.mapofzones.tokenmatcher.domain.Derivative;
import com.mapofzones.tokenmatcher.service.PathfinderFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@Import(PathfinderConfig.PathfinderRunner.class)
public class PathfinderConfig {

    @Bean
    public PathfinderProperties pathfinderProperties() {
        return new PathfinderProperties();
    }

    @Slf4j
    static class PathfinderRunner {

        private final PathfinderFacade pathfinderFacade;

        public PathfinderRunner(PathfinderFacade pathfinderFacade) {
            this.pathfinderFacade = pathfinderFacade;
        }

        @Scheduled(fixedDelayString = "#{pathfinderProperties.syncTime}")
        public void run() {
//            log.info("Pathfinder is running.");
//            Derivative derivative = new Derivative();
//            derivative.setDerivativeId(new Derivative.DerivativeId("crypto-org-chain-mainnet-1",
//                    "transfer/channel-44/transfer/channel-0/basecro"));
//
//
//            Derivative derivative = new Derivative();
//            derivative.setDerivativeId(new Derivative.DerivativeId("osmosis-1",
//                    "transfer/channel-2/udvpn"));
            pathfinderFacade.findAll();
            log.info("Pathfinder is finished.");
        }
    }
}
