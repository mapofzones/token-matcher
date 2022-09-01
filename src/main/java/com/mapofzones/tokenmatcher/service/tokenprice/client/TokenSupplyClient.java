package com.mapofzones.tokenmatcher.service.tokenprice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapofzones.tokenmatcher.common.properties.EndpointProperties;
import com.mapofzones.tokenmatcher.service.tokenprice.client.dto.SupplyTokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;

@Slf4j
public class TokenSupplyClient implements ITokenPriceClient {

    private final RestTemplate tokenPriceRestTemplate;
    private final EndpointProperties endpointProperties;

    public TokenSupplyClient(RestTemplate tokenPriceRestTemplate,
                             EndpointProperties endpointProperties) {
        this.tokenPriceRestTemplate = tokenPriceRestTemplate;
        this.endpointProperties = endpointProperties;
    }

    public SupplyTokenDto findTokenPrice(String urlWithBaseToken, LocalDateTime lastTokenPriceTime) {

        URI uri = URI.create(String.format(urlWithBaseToken, endpointProperties.getCosmosSDK().getSupply()));
        try {
            log.debug("Find supply: " + uri);
            ResponseEntity<String> response = tokenPriceRestTemplate.getForEntity(uri, String.class);
            JsonNode node = new ObjectMapper().readValue(response.getBody(), JsonNode.class);
            return new SupplyTokenDto(node.get("amount").get("amount").asText());
        } catch (JsonProcessingException | RestClientException e) {
            log.debug("Json can't parce: " + uri);
            return new SupplyTokenDto(null);
        }
    }
}
