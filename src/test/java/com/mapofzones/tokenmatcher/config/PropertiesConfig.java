package com.mapofzones.tokenmatcher.config;

import com.mapofzones.tokenmatcher.common.properties.EndpointProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@TestConfiguration
public class PropertiesConfig {

    @Bean
    public EndpointProperties endpointProperties() {
        return new EndpointProperties();
    }

}
