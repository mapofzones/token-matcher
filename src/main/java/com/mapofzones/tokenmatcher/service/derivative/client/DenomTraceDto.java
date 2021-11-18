package com.mapofzones.tokenmatcher.service.derivative.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonRootName(value = "denom_trace")
public class DenomTraceDto {
	@JsonProperty("path")
	private String path;
	@JsonProperty("base_denom")
	private String baseDenom;
	
}
