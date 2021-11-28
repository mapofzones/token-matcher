package com.mapofzones.tokenmatcher.config;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapofzones.tokenmatcher.common.properties.TokenMatcherProperties;
import com.mapofzones.tokenmatcher.service.derivative.client.DenomTraceClient;

@Configuration
public class DenomTraceClientConfig {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder
				.additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8))
				.build();
	}
	
	@Bean 
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		return objectMapper;
	}
	
	@Bean
	public DenomTraceClient denomTraceClient(RestTemplate restTemplate, 
			TokenMatcherProperties tokenMatcherProperties,
			ObjectMapper objectMapper) {
		return new DenomTraceClient(restTemplate, tokenMatcherProperties, objectMapper);
	}
}
