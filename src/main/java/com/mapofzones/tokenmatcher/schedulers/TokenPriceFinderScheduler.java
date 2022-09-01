package com.mapofzones.tokenmatcher.schedulers;

import com.mapofzones.tokenmatcher.service.TokenPriceFinderFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"dev", "prod"})
public class TokenPriceFinderScheduler {

    private final TokenPriceFinderFacade tokenPriceFinderFacade;

    public TokenPriceFinderScheduler(TokenPriceFinderFacade tokenPriceFinderFacade) {
        this.tokenPriceFinderFacade = tokenPriceFinderFacade;
    }

    private int iteration = 1;

    @Scheduled(fixedDelayString = "#{tokenPriceFinderProperties.syncTime}", initialDelayString = "6000")
    public void run() {
        log.info("[{}] TokenPriceFinder is running", iteration);
        tokenPriceFinderFacade.findAllTokenPrices();
        log.info("[{}] TokenPriceFinder is finishing", iteration);
        log.info("-----------------------------------");
        iteration++;
    }
}
