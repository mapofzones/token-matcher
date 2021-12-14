package com.mapofzones.tokenmatcher.service.derivative.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapofzones.tokenmatcher.common.exceptions.JsonParceException;
import com.mapofzones.tokenmatcher.common.properties.EndpointProperties;
import com.mapofzones.tokenmatcher.utils.UriHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
public class DenomTraceClient {

	private final RestTemplate denomTraceRestTemplate;
	private final EndpointProperties endpointProperties;
	private final ObjectMapper denomTraceObjectMapper;
		
	public DenomTraceClient(RestTemplate denomTraceRestTemplate,
							EndpointProperties endpointProperties,
							ObjectMapper denomTraceObjectMapper) {
		this.denomTraceRestTemplate = denomTraceRestTemplate;
		this.endpointProperties = endpointProperties;
		this.denomTraceObjectMapper = denomTraceObjectMapper;
	}

	// TODO: Need to refactoring when findRest wos implemented
	public DenomTraceDto findDenomTrace(String address, String hash) {

		if (!address.isEmpty()) {
			URI uri = URI.create(address + String.format(endpointProperties.getIbc().getDenomTrace(), hash));
			log.info(String.valueOf((uri)));

			try {
				ResponseEntity<String> response = denomTraceRestTemplate.getForEntity(uri, String.class);
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
			return denomTraceObjectMapper.readValue(json, DenomTraceDto.class);
		} catch (JsonProcessingException e) {
			log.warn("Cant parse json. ");
			throw new JsonParceException("Cant parse json", e.getCause());
		}
	}
	
}
