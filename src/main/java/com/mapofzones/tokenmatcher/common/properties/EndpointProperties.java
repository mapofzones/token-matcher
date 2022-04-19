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
    private Osmosis osmosis;

    @Getter
    @Setter
    public static class IBC {
        private String denomTraceBeta;
        private String denomTrace;
    }

    @Getter
    @Setter
    public static class Coingecko {
        private String baseUrl;
        private String tokenPriceHistory;
    }

    @Getter
    @Setter
    public static class Osmosis {
        private String baseUrl;
        private String tokenPriceHistory;
        private String tokenPriceHistoryV2;
    }
}
