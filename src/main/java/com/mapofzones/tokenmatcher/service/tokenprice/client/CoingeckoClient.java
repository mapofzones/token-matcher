package com.mapofzones.tokenmatcher.service.tokenprice.client;

import com.mapofzones.tokenmatcher.common.exceptions.JsonParceException;
import com.mapofzones.tokenmatcher.common.properties.EndpointProperties;
import com.mapofzones.tokenmatcher.service.tokenprice.client.dto.TokenPriceInSpecificHourDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CoingeckoClient {

    private static final int TIME_POSITION = 0;
    private static final int PRICE_POSITION = 1;
    private static final String NAME_OF_PRICE_ARRAY = "prices";


    private final RestTemplate tokenPriceRestTemplate;
    private final EndpointProperties endpointProperties;

    public CoingeckoClient(RestTemplate tokenPriceRestTemplate,
                           EndpointProperties endpointProperties) {
        this.tokenPriceRestTemplate = tokenPriceRestTemplate;
        this.endpointProperties = endpointProperties;
    }

    public List<TokenPriceInSpecificHourDto> findTokenPrice(String coingeckoId, Long beginTimeInDays) {
        URI uri = URI.create(String.format(endpointProperties.getCoingecko().baseUrl + endpointProperties.getCoingecko().tokenPriceHistory,
                coingeckoId, beginTimeInDays));

        if (!uri.toString().isEmpty()) {
            try {
                ResponseEntity<String> response = tokenPriceRestTemplate.getForEntity(uri, String.class);
                return jsonToDto(response.getBody());
            } catch (RestClientException e) {
                log.warn("(CoingeckoClient)Request cant be completed. " + uri);
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    private List<TokenPriceInSpecificHourDto> jsonToDto(String json) {
        try {
            JSONObject priceJson = new JSONObject(json);
            JSONArray priceArray = priceJson.getJSONArray(NAME_OF_PRICE_ARRAY);

            List<TokenPriceInSpecificHourDto> tokenPriceList = new ArrayList<>();
            for (int i= 0; i < priceArray.length(); i++) {
                JSONArray priceAnHour = priceArray.getJSONArray(i);
                Long time = null;
                BigDecimal price = null;
                for (int j = 0; j < priceArray.length(); j++) {
                    time = priceAnHour.getLong(TIME_POSITION);
                    price = BigDecimal.valueOf(priceAnHour.getDouble(PRICE_POSITION));
                }
                tokenPriceList.add(new TokenPriceInSpecificHourDto(time, price));
            }

            return tokenPriceList;
        } catch (JSONException e) {
            log.warn("Cant parse json.");
            throw new JsonParceException("Cant parse json", e.getCause());
        }
    }
}
