package com.mapofzones.tokenmatcher.service.derivative.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonRootName(value = "denom_trace")
public class DenomTraceDto {

	public DenomTraceDto(boolean isSuccessReceived) {
		this.isSuccessReceived = isSuccessReceived;
	}

	@JsonProperty("path")
	private String fullDenom;

	@JsonProperty("base_denom")
	private String baseDenom;

	@JsonIgnore
	private boolean isSuccessReceived;
}