package com.mapofzones.tokenmatcher.config;

import com.mapofzones.tokenmatcher.common.properties.TokenMatcherProperties;
import com.mapofzones.tokenmatcher.common.threads.IThreadStarter;
import com.mapofzones.tokenmatcher.common.threads.ThreadStarter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Slf4j
@Configuration
public class TokenMatcherConfig {
	
	@Bean
	public TokenMatcherProperties tokenMatcherProperties() {
		return new TokenMatcherProperties();
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public IThreadStarter tokenMatcherThreadStarter() {
		return new ThreadStarter(tokenMatcherProperties().getThreads());
	}
}
