package com.mapofzones.tokenmatcher.service;

import com.mapofzones.tokenmatcher.common.threads.IThreadStarter;
import com.mapofzones.tokenmatcher.domain.Token;
import com.mapofzones.tokenmatcher.domain.TokenPrice;
import com.mapofzones.tokenmatcher.service.token.ITokenService;
import com.mapofzones.tokenmatcher.service.tokenprice.ITokenPriceService;
import com.mapofzones.tokenmatcher.service.tokenprice.client.CoingeckoClient;
import com.mapofzones.tokenmatcher.service.tokenprice.client.dto.TokenPriceInSpecificHourDto;
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
        if (!unprocessedTokens.isEmpty()) {
            cashflowQueue = new ArrayBlockingQueue<>(unprocessedTokens.size(), true, unprocessedTokens);
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
                    findTokenPrice(token);
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
