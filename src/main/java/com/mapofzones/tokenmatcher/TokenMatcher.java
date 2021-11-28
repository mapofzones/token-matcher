package com.mapofzones.tokenmatcher;

import com.mapofzones.tokenmatcher.common.properties.TokenMatcherProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@EnableConfigurationProperties(value = {TokenMatcherProperties.class})
public class TokenMatcher {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(TokenMatcher.class, args);
	}
}
