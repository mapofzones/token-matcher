package com.mapofzones.tokenmatcher.service.tokenprice.client;

import com.mapofzones.tokenmatcher.common.exceptions.JsonParceException;
import com.mapofzones.tokenmatcher.common.properties.EndpointProperties;
import com.mapofzones.tokenmatcher.service.tokenprice.client.dto.TokenPriceInSpecificHourDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CoingeckoClient {

    // 2021-01-01
    private final static Long START_DATE_IN_MILLIS = 1609448400000L;
    private final static Long MILLIS_IN_DAY = 86400000L;
    private final static Long DAYS_IN_RANGE = 50 * MILLIS_IN_DAY;

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

    public List<TokenPriceInSpecificHourDto> findTokenPrice(String coingeckoId) {

        List<URI> uriList = prepareURLForFindPartsOfRange(coingeckoId);
        log.info("coingecko URIs size: " + uriList.size());
        List<TokenPriceInSpecificHourDto> dtoList = new ArrayList<>();
        if (!uriList.isEmpty()) {
            uriList.forEach(uri -> {
                log.info("coingecko URI: " + uri.toString());
                try {
                    ResponseEntity<String> response = tokenPriceRestTemplate.getForEntity(uri, String.class);
                    dtoList.addAll(jsonToDto(response.getBody()));
                } catch (RestClientException e) {
                    log.warn("(CoingeckoClient)Request cant be completed. " + uri);
                }
            });
        }
        log.info("DTOs size: " + dtoList.size());
        return dtoList;
    }

    // TODO Need to refactoring
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
        } catch (Exception e) {
            log.warn("Cant parse json.");
            throw new JsonParceException("Cant parse json", e.getCause());
        }
    }

    private List<URI> prepareURLForFindPartsOfRange(String coingeckoId) {

        List<URI> uriList = new ArrayList<>();

        List<Long> days = new ArrayList<>();
        days.add(START_DATE_IN_MILLIS);

        Long allDaysInMillis = calculateDaysBetweenStartDateAndNow();

        Long countMillis = START_DATE_IN_MILLIS;
        for (int i = 0; i < allDaysInMillis / DAYS_IN_RANGE; i++) {
            countMillis = countMillis + DAYS_IN_RANGE;
            days.add(countMillis);
        }

        days.forEach(d -> {
            uriList.add(URI.create(
                    String.format(
                            endpointProperties.getCoingecko().getBaseUrl() + endpointProperties.getCoingecko().getTokenPriceHistory(),
                            coingeckoId, d / 1000, (d + DAYS_IN_RANGE) / 1000)));
        });

        return uriList;
    }

    private long calculateDaysBetweenStartDateAndNow() {
        LocalDateTime startDate = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(START_DATE_IN_MILLIS), ZoneId.systemDefault()).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.MILLIS.between(startDate, now);
    }

}
