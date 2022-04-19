package com.mapofzones.tokenmatcher.config;

import com.mapofzones.tokenmatcher.common.properties.TokenPriceFinderProperties;
import com.mapofzones.tokenmatcher.common.threads.IThreadStarter;
import com.mapofzones.tokenmatcher.common.threads.ThreadStarter;
import com.mapofzones.tokenmatcher.service.token.ITokenService;
import com.mapofzones.tokenmatcher.service.tokenprice.DexEnum;
import com.mapofzones.tokenmatcher.service.tokenprice.services.ITokenPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class TokenPriceFinderConfig {

    @Bean
    public TokenPriceFinderProperties tokenPriceFinderProperties() {
        return new TokenPriceFinderProperties();
    }

    @Bean
    public IThreadStarter tokenPriceFinderThreadStarter() {
        return new ThreadStarter(tokenPriceFinderProperties().getThreads(), tokenPriceFinderProperties().getThreadsNaming());
    }

    @Bean
    public Map<DexEnum, ITokenService> mapTokenService(@Qualifier("coingeckoTokenService") ITokenService coingeckoTokenService,
                                                       @Qualifier("osmosisTokenService") ITokenService osmosisTokenService) {
        Map<DexEnum, ITokenService> map = new HashMap<>();
        map.put(DexEnum.COINGECKO, coingeckoTokenService);
        map.put(DexEnum.OSMOSIS, osmosisTokenService);
        return map;
    }

    @Bean
    public Map<DexEnum, ITokenPriceService> mapTokenPriceService(@Qualifier("coingeckoTokenPriceService") ITokenPriceService coingeckoTokenPriceService,
                                                                 @Qualifier("osmosisTokenPriceService") ITokenPriceService osmosisTokenPriceService) {
        Map<DexEnum, ITokenPriceService> map = new HashMap<>();
        map.put(DexEnum.COINGECKO, coingeckoTokenPriceService);
        map.put(DexEnum.OSMOSIS, osmosisTokenPriceService);
        return map;
    }
}
