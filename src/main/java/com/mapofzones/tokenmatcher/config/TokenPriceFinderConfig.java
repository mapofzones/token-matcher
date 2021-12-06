package com.mapofzones.tokenmatcher.config;

import com.mapofzones.tokenmatcher.common.properties.TokenPriceFinderProperties;
import com.mapofzones.tokenmatcher.common.threads.IThreadStarter;
import com.mapofzones.tokenmatcher.common.threads.ThreadStarter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class TokenPriceFinderConfig {

    @Bean
    public TokenPriceFinderProperties tokenFinderProperties() {
        return new TokenPriceFinderProperties();
    }

    @Bean
    public IThreadStarter tokenPriceFinderThreadStarter() {
        return new ThreadStarter(tokenFinderProperties().getThreads(), tokenFinderProperties().getThreadsNaming());
    }
}
