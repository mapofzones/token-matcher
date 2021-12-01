package com.mapofzones.tokenmatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TokenMatcher {

	public static void main(String[] args) {
		SpringApplication.run(TokenMatcher.class, args);
	}
}
