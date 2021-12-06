package com.mapofzones.tokenmatcher.schedulers;

import com.mapofzones.tokenmatcher.common.threads.IThreadStarter;
import com.mapofzones.tokenmatcher.service.TokenPriceFinderFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenPriceFinderScheduler {

    private final TokenPriceFinderFacade tokenPriceFinderFacade;
    private final IThreadStarter tokenPriceFinderThreadStarter;

    public TokenPriceFinderScheduler(TokenPriceFinderFacade tokenPriceFinderFacade,
                                     IThreadStarter tokenPriceFinderThreadStarter) {
        this.tokenPriceFinderFacade = tokenPriceFinderFacade;
        this.tokenPriceFinderThreadStarter = tokenPriceFinderThreadStarter;
    }

    @Scheduled(fixedDelayString = "#{tokenFinderProperties.syncTime}")
    public void run() {

        //tokenPriceFinderFacade.findTokenPrice(null);
        if (tokenPriceFinderThreadStarter.isDone()) {
            log.info("TokenFinder is running.");
            tokenPriceFinderFacade.findAllTokenPrices();
        }
    }

}
