package com.mapofzones.tokenmatcher.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "endpoint")
public class EndpointProperties {

    private IBC ibc;
    private Coingecko coingecko;

    @Getter
    @Setter
    public static class IBC {
        private String denomTrace;
    }

    @Getter
    @Setter
    public static class Coingecko {
        private String baseUrl;
        private String tokenPriceHistory;
    }
}
