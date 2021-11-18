package com.mapofzones.tokenmatcher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;

import com.mapofzones.tokenmatcher.common.properties.TokenMatcherProperties;
import com.mapofzones.tokenmatcher.service.TokenMatcherFacade;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Import(TokenMatcherConfig.TokenMatcherRunner.class)
public class TokenMatcherConfig {
	
	@Bean
	public TokenMatcherProperties tokenMatcherProperties() {
		return new TokenMatcherProperties();
	}
	
	@Slf4j
	static class TokenMatcherRunner {
		
		private final TokenMatcherFacade tokenMatcherFacade;
		
		public TokenMatcherRunner(TokenMatcherFacade tokenMatcherFacade) {
			this.tokenMatcherFacade = tokenMatcherFacade;
		}
		
		@Scheduled(fixedDelayString = "#{tokenMatcherProperties.syncTime}")
		public void run() {
			log.info("TokenMatcher is running.");
			tokenMatcherFacade.match();
			log.info("TokenMatcher is running.");
		}
	}
}
