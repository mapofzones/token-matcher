package com.mapofzones.tokenmatcher.service;

import com.mapofzones.tokenmatcher.common.threads.IThreadStarter;
import com.mapofzones.tokenmatcher.domain.Token;
import com.mapofzones.tokenmatcher.service.token.ITokenService;
import com.mapofzones.tokenmatcher.service.tokenprice.ITokenPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Service
public class TokenPriceFinderFacade {

    private final ITokenService tokenService;
    private final IThreadStarter tokenPriceFinderThreadStarter;
    private final ITokenPriceService tokenPriceService;

    private BlockingQueue<Token> cashflowQueue;

    public TokenPriceFinderFacade(ITokenService tokenService,
                                  IThreadStarter tokenPriceFinderThreadStarter,
                                  ITokenPriceService tokenPriceService) {
        this.tokenService = tokenService;
        this.tokenPriceFinderThreadStarter = tokenPriceFinderThreadStarter;
        this.tokenPriceService = tokenPriceService;
    }

    public void findAllTokenPrices() {
        List<Token> unprocessedTokens = tokenService.FindAllByCoingeckoIsNotNull();
        log.info("Tokens size: " + unprocessedTokens.size());
        if (!unprocessedTokens.isEmpty()) {
            cashflowQueue = new ArrayBlockingQueue<>(unprocessedTokens.size(), true, unprocessedTokens);
            log.info("Cashflow queue size: " + cashflowQueue.size());
            tokenPriceFinderThreadStarter.startThreads(findTokenPriceFunction);
        }
    }

    @Transactional
    public void findTokenPrice(Token token) {
        tokenPriceService.findAndSaveTokenPriceByToken(token);
    }

    private final Runnable findTokenPriceFunction = () -> {
        while (true) {
            if (!cashflowQueue.isEmpty()) {
                try {
                    Token token = cashflowQueue.take();
                    log.info("...Start findTokenPrice for zone: " + token.getTokenId().getZone());
                    findTokenPrice(token);
                    log.info("...Finished findTokenPrice" + token.getTokenId().getZone());
                    log.info(Thread.currentThread().getName() + " Start matching " + token);
                } catch (InterruptedException e) {
                    log.error("Queue error. " + e.getCause());
                    e.printStackTrace();
                }
            }
            else break;
        }
    };

}
