package com.mapofzones.tokenmatcher.service.denomtraces.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@JsonRootName(value = "denom_trace")
public class DenomTraceDto {

	public DenomTraceDto(boolean isSuccessReceived) {
		this.isSuccessReceived = isSuccessReceived;
	}

	public DenomTraceDto(String path, String baseDenom, boolean isSuccessReceived) {
		this.path = path;
		this.baseDenom = baseDenom;
		this.isSuccessReceived = isSuccessReceived;
	}

	@JsonProperty("path")
	private String path;

	@JsonProperty("base_denom")
	private String baseDenom;

	@JsonIgnore
	private String ibcHash;

	@JsonIgnore
	private boolean isSuccessReceived;
}
