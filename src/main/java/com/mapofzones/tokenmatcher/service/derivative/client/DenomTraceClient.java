package com.mapofzones.tokenmatcher.service.derivative.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapofzones.tokenmatcher.common.exceptions.JsonParceException;
import com.mapofzones.tokenmatcher.common.properties.TokenMatcherProperties;
import com.mapofzones.tokenmatcher.utils.UriHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
public class DenomTraceClient {

	private final RestTemplate restTemplate;
	private final TokenMatcherProperties tokenMatcherProperties;
	private final ObjectMapper objectMapper;
		
	public DenomTraceClient(RestTemplate restTemplate, 
			TokenMatcherProperties tokenMatcherProperties,
			ObjectMapper objectMapper) {
		this.restTemplate = restTemplate;
		this.tokenMatcherProperties = tokenMatcherProperties;
		this.objectMapper = objectMapper;
	}

	// TODO: Need to refactoring when findRest wos implemented
	public DenomTraceDto findDenomTrace(String address, String hash) {

		URI uri = UriHelper.modifyUri(URI.create(address + String.format(tokenMatcherProperties.getEndpoint(), hash)));
		log.info(String.valueOf((uri)));
		if (!uri.toString().isEmpty()) {

			try {
				ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
				DenomTraceDto receivedDenomTraceDto = jsonToDto(response.getBody());
				receivedDenomTraceDto.setSuccessReceived(true);
				return receivedDenomTraceDto;
			} catch (RestClientException e) {
				log.warn("Request cant be completed. " + uri);
				return new DenomTraceDto(false);
			}
		}
		return new DenomTraceDto();
	}
	
	private DenomTraceDto jsonToDto(String json) {
		try {
			return objectMapper.readValue(json, DenomTraceDto.class);
		} catch (JsonProcessingException e) {
			log.warn("Cant parse json. ");
			throw new JsonParceException("Cant parse json", e.getCause());
		}
	}
	
}
