package com.mapofzones.tokenmatcher.service;

import com.mapofzones.tokenmatcher.common.threads.IThreadStarter;
import com.mapofzones.tokenmatcher.domain.token.Token;
import com.mapofzones.tokenmatcher.service.token.ITokenService;
import com.mapofzones.tokenmatcher.service.tokenprice.DexEnum;
import com.mapofzones.tokenmatcher.service.tokenprice.services.ITokenPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final ITokenService tokenService;

    private ITokenPriceService tokenPriceService;

    private BlockingQueue<Token> queueIds;

    public TokenPriceFinderFacade(Map<DexEnum, ITokenService> mapTokenService,
                                  @Qualifier("tokenPriceFinderThreadStarter") IThreadStarter tokenPriceFinderThreadStarter,
                                  Map<DexEnum, ITokenPriceService> mapTokenPriceService,
                                  ITokenService tokenService) {
        this.mapTokenService = mapTokenService;
        this.tokenPriceFinderThreadStarter = tokenPriceFinderThreadStarter;
        this.mapTokenPriceService = mapTokenPriceService;
        this.tokenService = tokenService;
    }

    public void findAllTokenPrices() {

        for (Map.Entry<DexEnum, ITokenPriceService> entry : mapTokenPriceService.entrySet()) {
            tokenPriceService = entry.getValue();
            List<Token> unprocessedCoingeckoIds = mapTokenService.get(entry.getKey()).findAllByDexIdIsNotNull();
            log.debug("Unprocessed tokens size: {} - {}", entry.getKey(), unprocessedCoingeckoIds.size());

            if (!unprocessedCoingeckoIds.isEmpty()) {
                queueIds = new ArrayBlockingQueue<>(unprocessedCoingeckoIds.size(), true, unprocessedCoingeckoIds);
                log.debug("Price queue size: " + queueIds.size());
                tokenPriceFinderThreadStarter.startThreads(findTokenPriceInDexFunction);
            }
        }
    }

    private final Runnable findTokenPriceInDexFunction = () -> queueIds.stream().parallel().forEach(node -> {
        try {
            Token token = queueIds.take();
            log.debug("...Start findTokenPrice in coingecko for zone: " + token.getTokenId().getZone() +
                    " token: " + token.getTokenId().getBaseDenom());
            Integer foundPrices = findTokenPriceByDexId(token);
            log.debug("...Finished findTokenPrice in coingecko" + token.getTokenId().getZone());
            log.debug(Thread.currentThread().getName() + " Start matching " + token);
            updateTokenPriceLastCheckedAt(foundPrices, token);
        } catch (InterruptedException e) {
            log.error("Queue error. " + e.getCause());
            Thread.currentThread().interrupt();
        }
    });


    @Transactional
    public Integer findTokenPriceByDexId(Token token) {
        return tokenPriceService.findAndSaveTokenPrice(token);
    }

    @Transactional
    public void updateTokenPriceLastCheckedAt(Integer foundPrices, Token token) {
        if (foundPrices > 0) {
            token.setPriceLastCheckedAt(LocalDateTime.now());
            tokenService.save(token);
        }

    }
}
