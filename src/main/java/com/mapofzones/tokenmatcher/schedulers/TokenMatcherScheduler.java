package com.mapofzones.tokenmatcher.schedulers;

import com.mapofzones.tokenmatcher.service.TokenMatcherFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"dev", "prod"})
public class TokenMatcherScheduler {

    private final TokenMatcherFacade tokenMatcherFacade;

    public TokenMatcherScheduler(TokenMatcherFacade tokenMatcherFacade) {
        this.tokenMatcherFacade = tokenMatcherFacade;
    }

    private int iteration = 1;

    @Scheduled(fixedDelayString = "#{tokenMatcherProperties.syncTime}", initialDelayString = "4000")
    public void run() {
        log.info("[{}] TokenMatcher is running", iteration);
        tokenMatcherFacade.matchAll();
        log.info("[{}] TokenMatcher is finishing", iteration);
        log.info("-----------------------------------");
        iteration++;
    }
}
