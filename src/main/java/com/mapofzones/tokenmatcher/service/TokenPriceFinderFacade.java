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

    private BlockingQueue<Token> coingeckoIds;
    private BlockingQueue<Token> osmosisIds;

    public TokenPriceFinderFacade(ITokenService tokenService,
                                  IThreadStarter tokenPriceFinderThreadStarter,
                                  ITokenPriceService tokenPriceService) {
        this.tokenService = tokenService;
        this.tokenPriceFinderThreadStarter = tokenPriceFinderThreadStarter;
        this.tokenPriceService = tokenPriceService;
    }

    public void findAllTokenPrices() {
        List<Token> unprocessedCoingeckoIds = tokenService.findAllByCoingeckoIsNotNull();
        log.info("Tokens size: " + unprocessedCoingeckoIds.size());
        if (!unprocessedCoingeckoIds.isEmpty()) {
            coingeckoIds = new ArrayBlockingQueue<>(unprocessedCoingeckoIds.size(), true, unprocessedCoingeckoIds);
            log.info("Cashflow queue size: " + coingeckoIds.size());
            tokenPriceFinderThreadStarter.startThreads(findTokenPriceInCoingeckoFunction);
            tokenPriceFinderThreadStarter.waitMainThread();
        }

        List<Token> unprocessedOsmosisTokens = tokenService.findAllByOsmosisIsNotNull();
        log.info("Tokens size: " + unprocessedOsmosisTokens.size());
        if (!unprocessedOsmosisTokens.isEmpty()) {
            osmosisIds = new ArrayBlockingQueue<>(unprocessedOsmosisTokens.size(), true, unprocessedOsmosisTokens);
            log.info("Cashflow queue size: " + osmosisIds.size());
            tokenPriceFinderThreadStarter.startThreads(findTokenPriceInOsmosisFunction);
            tokenPriceFinderThreadStarter.waitMainThread();
        }

    }

    @Transactional
    public void findTokenPriceByCoingeckoId(Token token) {
        tokenPriceService.findAndSaveTokenPriceByCoingeckoId(token);
    }

    @Transactional
    public void findTokenPriceByOsmosisId(Token token) {
        tokenPriceService.findAndSaveTokenPriceByOsmosisId(token);
    }

    private final Runnable findTokenPriceInCoingeckoFunction = () -> {
        while (true) {
            if (!coingeckoIds.isEmpty()) {
                try {
                    Token token = coingeckoIds.take();
                    log.info("...Start findTokenPrice in coingecko for zone: " + token.getTokenId().getZone());
                    findTokenPriceByCoingeckoId(token);
                    log.info("...Finished findTokenPrice in coingecko" + token.getTokenId().getZone());
                    log.info(Thread.currentThread().getName() + " Start matching " + token);
                } catch (InterruptedException e) {
                    log.error("Queue error. " + e.getCause());
                    e.printStackTrace();
                }
            }
            else break;
        }
    };

    private final Runnable findTokenPriceInOsmosisFunction = () -> {
        while (true) {
            if (!osmosisIds.isEmpty()) {
                try {
                    Token token = osmosisIds.take();
                    log.info("...Start findTokenPrice in osmosis for zone: " + token.getTokenId().getZone());
                    findTokenPriceByOsmosisId(token);
                    log.info("...Finished findTokenPrice in osmosis" + token.getTokenId().getZone());
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
