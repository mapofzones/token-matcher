package com.mapofzones.tokenmatcher.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "token-matcher")
public class TokenMatcherProperties extends BaseProperties {


}
