package com.mapofzones.tokenmatcher;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mapofzones.tokenmatcher.common.properties.TokenMatcherProperties;
import com.mapofzones.tokenmatcher.domain.Derivative;
import com.mapofzones.tokenmatcher.service.cashflow.CashflowService;
import com.mapofzones.tokenmatcher.service.derivative.DerivativeService;
import com.mapofzones.tokenmatcher.service.zonenode.ZoneNodeService;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(value = {TokenMatcherProperties.class})
@Slf4j
public class TokenMatcher {

	public static void main(String[] args) {		
		
		ApplicationContext context = SpringApplication.run(TokenMatcher.class, args);
//		CashflowService cfs = (CashflowService) context.getBean("cashflowService");
//		ZoneNodeService zns = (ZoneNodeService) context.getBean("zoneNodeService");
//		DerivativeService ds = (DerivativeService) context.getBean("derivativeService");
//		Derivative ds1 = ds.match(null);
		
//		System.out.println("111");
	}

}
