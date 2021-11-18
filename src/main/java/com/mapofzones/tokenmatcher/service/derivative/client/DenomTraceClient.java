package com.mapofzones.tokenmatcher.service.derivative.client;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapofzones.tokenmatcher.common.exceptions.JsonParceException;
import com.mapofzones.tokenmatcher.common.properties.TokenMatcherProperties;
import com.mapofzones.tokenmatcher.utils.UriHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DenomTraceClient {

	private RestTemplate restTemplate;
	private TokenMatcherProperties tokenMatcherProperties;
	private ObjectMapper objectMapper;
		
	public DenomTraceClient(RestTemplate restTemplate, 
			TokenMatcherProperties tokenMatcherProperties,
			ObjectMapper objectMapper) {
		this.restTemplate = restTemplate;
		this.tokenMatcherProperties = tokenMatcherProperties;
		this.objectMapper = objectMapper;
	}

	public DenomTraceDto findDenomTrace(String address, String hash) {

		URI uri = UriHelper.modifyUri(URI.create(address + String.format(tokenMatcherProperties.getEndpoint(), hash)));
		log.info((uri.toString()));
		if (!uri.toString().isEmpty()) {
			ResponseEntity<String> responce = restTemplate.getForEntity(uri, String.class);
			if (!responce.getStatusCode().equals(HttpStatus.NOT_FOUND))
				return jsonToDto(responce.getBody());
		}
		return new DenomTraceDto();
	}
	
	private DenomTraceDto jsonToDto(String json) {
		try {
			return objectMapper.readValue(json, DenomTraceDto.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonParceException("Cant parce json", e.getCause());
		}
	}
	
}
