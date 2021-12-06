package com.mapofzones.tokenmatcher.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "token-price-finder")
public class TokenPriceFinderProperties extends BaseProperties {


}
