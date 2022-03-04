package com.mapofzones.tokenmatcher.service;

import com.mapofzones.tokenmatcher.common.threads.IThreadStarter;
import com.mapofzones.tokenmatcher.domain.token.Token;
import com.mapofzones.tokenmatcher.service.token.ITokenService;
import com.mapofzones.tokenmatcher.service.tokenprice.DexEnum;
import com.mapofzones.tokenmatcher.service.tokenprice.services.ITokenPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Service
public class TokenPriceFinderFacade {

    private final IThreadStarter tokenPriceFinderThreadStarter;
    private final Map<DexEnum, ITokenPriceService> mapTokenPriceService;
    private final Map<DexEnum, ITokenService> mapTokenService;

    private ITokenPriceService tokenPriceService;

    private BlockingQueue<Token> queueIds;

    public TokenPriceFinderFacade(Map<DexEnum, ITokenService> mapTokenService,
                                  IThreadStarter tokenPriceFinderThreadStarter,
                                  Map<DexEnum, ITokenPriceService> mapTokenPriceService) {
        this.mapTokenService = mapTokenService;
        this.tokenPriceFinderThreadStarter = tokenPriceFinderThreadStarter;
        this.mapTokenPriceService = mapTokenPriceService;
    }

    public void findAllTokenPrices() {

        for (Map.Entry<DexEnum, ITokenPriceService> entry : mapTokenPriceService.entrySet()) {
            tokenPriceService = entry.getValue();
            List<Token> unprocessedCoingeckoIds = mapTokenService.get(entry.getKey()).findAllByDexIdIsNotNull();
            log.info("Tokens size: " + unprocessedCoingeckoIds.size());

            if (!unprocessedCoingeckoIds.isEmpty()) {
                queueIds = new ArrayBlockingQueue<>(unprocessedCoingeckoIds.size(), true, unprocessedCoingeckoIds);
                log.info("Price queue size: " + queueIds.size());
                tokenPriceFinderThreadStarter.startThreads(findTokenPriceInDexFunction);
                tokenPriceFinderThreadStarter.waitMainThread();
            }
        }
    }

    @Transactional
    public void findTokenPriceByDexId(Token token) {
        tokenPriceService.findAndSaveTokenPrice(token);
    }

    private final Runnable findTokenPriceInDexFunction = () -> {
        while (true) {
            if (!queueIds.isEmpty()) {
                try {
                    Token token = queueIds.take();
                    log.info("...Start findTokenPrice in coingecko for zone: " + token.getTokenId().getZone());
                    findTokenPriceByDexId(token);
                    log.info("...Finished findTokenPrice in coingecko" + token.getTokenId().getZone());
                    log.info(Thread.currentThread().getName() + " Start matching " + token);
                } catch (InterruptedException e) {
                    log.error("Queue error. " + e.getCause());
                    Thread.currentThread().interrupt();
                }
            }
            else break;
        }
    };
}
