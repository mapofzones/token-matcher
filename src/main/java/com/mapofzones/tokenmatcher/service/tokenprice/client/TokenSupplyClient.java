package com.mapofzones.tokenmatcher.service.tokenprice.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapofzones.tokenmatcher.common.properties.EndpointProperties;
import com.mapofzones.tokenmatcher.service.tokenprice.client.dto.SupplyTokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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

        Optional<String> supply = callApi(uri.toString(), false);

        if (supply.isEmpty()) {
            uri = URI.create(String.format(urlWithBaseToken, endpointProperties.getCosmosSDK().getSupplyPaginated()));
            supply = callApi(uri.toString(), true);
        }

        return new SupplyTokenDto(supply.orElse(null));
    }

    private Optional<String> callApi(String uri, boolean arraySupply) {
        try {
            log.debug("Find supply: " + uri);

            if (arraySupply) {
                String denom = uri.substring(uri.lastIndexOf("/") + 1);
                uri = uri.substring(0, uri.lastIndexOf("/"));

                ResponseEntity<String> response = tokenPriceRestTemplate.getForEntity(uri, String.class);
                JsonNode arrayNode = new ObjectMapper().readValue(response.getBody(), JsonNode.class);
                Iterator<JsonNode> iter = arrayNode.findValue("supply").elements();
                List<JsonNode> jsonNodes = new ArrayList<>();
                iter.forEachRemaining(jsonNodes::add);

                JsonNode node = null;
                for (JsonNode jsonNode : jsonNodes) {
                    if (jsonNode.get("denom").asText().equals(denom)) {
                        node = jsonNode;
                        break;
                    }
                }

                return Optional.of(node.get("amount").asText());
            } else {
                ResponseEntity<String> response = tokenPriceRestTemplate.getForEntity(uri, String.class);
                JsonNode node = new ObjectMapper().readValue(response.getBody(), JsonNode.class);
                return Optional.of(node.get("amount").get("amount").asText());
            }

        } catch (Exception e) {
            log.debug("Json can't parce: " + uri);
            return Optional.empty();
        }
    }

}
