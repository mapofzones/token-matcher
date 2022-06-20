package com.mapofzones.tokenmatcher.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapofzones.tokenmatcher.common.properties.EndpointProperties;
import com.mapofzones.tokenmatcher.service.tokenprice.client.CoingeckoClient;
import com.mapofzones.tokenmatcher.service.tokenprice.client.OsmosisClient;
import com.mapofzones.tokenmatcher.service.tokenprice.client.TokenSupplyClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Configuration
public class TokenPriceClientConfig {

    @Bean
    public RestTemplate tokenPriceRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8))
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Bean
    public ObjectMapper tokenPriceObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        return objectMapper;
    }

    @Bean
    public CoingeckoClient coingeckoClient(RestTemplate tokenPriceRestTemplate,
                                           EndpointProperties endpointProperties) {
        return new CoingeckoClient(tokenPriceRestTemplate, endpointProperties);
    }

    @Bean
    public OsmosisClient osmosisClient(RestTemplate tokenPriceRestTemplate,
                                       EndpointProperties endpointProperties) {
        return new OsmosisClient(tokenPriceRestTemplate, endpointProperties);
    }

    @Bean
    public TokenSupplyClient supplyClient(RestTemplate tokenPriceRestTemplate,
                                          EndpointProperties endpointProperties) {
        return new TokenSupplyClient(tokenPriceRestTemplate, endpointProperties);
    }

}
