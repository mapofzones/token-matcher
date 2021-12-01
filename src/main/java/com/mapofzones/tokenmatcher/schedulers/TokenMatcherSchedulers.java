package com.mapofzones.tokenmatcher.schedulers;

import com.mapofzones.tokenmatcher.common.threads.IThreadStarter;
import com.mapofzones.tokenmatcher.service.TokenMatcherFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenMatcherSchedulers {

    private final TokenMatcherFacade tokenMatcherFacade;
    private final IThreadStarter tokenMatcherThreadStarter;

    public TokenMatcherSchedulers(TokenMatcherFacade tokenMatcherFacade,
                                  IThreadStarter tokenMatcherThreadStarter) {
        this.tokenMatcherFacade = tokenMatcherFacade;
        this.tokenMatcherThreadStarter = tokenMatcherThreadStarter;
    }

    @Scheduled(fixedDelayString = "#{tokenMatcherProperties.syncTime}")
    public void run() {
        if (tokenMatcherThreadStarter.isDone()) {
            log.info("TokenMatcher is running.");
            tokenMatcherFacade.matchAll();
        }
    }
}
