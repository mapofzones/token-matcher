package com.mapofzones.tokenmatcher.common.properties;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "token-matcher")
public class TokenMatcherProperties extends BaseProperties {

	private String endpoint;
	private Integer batchSize;

}
