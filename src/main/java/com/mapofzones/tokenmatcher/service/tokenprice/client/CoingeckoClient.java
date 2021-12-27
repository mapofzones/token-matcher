package com.mapofzones.tokenmatcher.service.tokenprice.client;

import com.mapofzones.tokenmatcher.common.properties.EndpointProperties;
import com.mapofzones.tokenmatcher.service.tokenprice.client.dto.TokenPriceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class CoingeckoClient {

    // 2021-01-01
    private final static Long START_DATE_IN_MILLIS = 1609448400000L;
    private final static Long MILLIS_IN_DAY = 86400000L;
    private final static Long DAYS_IN_RANGE = 50 * MILLIS_IN_DAY;

    private final RestTemplate tokenPriceRestTemplate;
    private final EndpointProperties endpointProperties;

    public CoingeckoClient(RestTemplate tokenPriceRestTemplate,
                           EndpointProperties endpointProperties) {
        this.tokenPriceRestTemplate = tokenPriceRestTemplate;
        this.endpointProperties = endpointProperties;
    }

    public TokenPriceDto findTokenPrice(String coingeckoId) {

        List<URI> uriList = prepareURLForFindPartsOfRange(coingeckoId);
        log.info("coingecko URIs size: " + uriList.size());
        TokenPriceDto foundPrices = new TokenPriceDto();
        if (!uriList.isEmpty()) {
            for (URI uri : uriList) {
                log.info("coingecko URI: " + uri.toString());
                try {
                    ResponseEntity<TokenPriceDto> response = tokenPriceRestTemplate.getForEntity(uri, TokenPriceDto.class);
                    foundPrices.addPricesRows(Objects.requireNonNull(response.getBody()).getPrices());
                } catch (RestClientException e) {
                    log.warn("(CoingeckoClient)Request cant be completed. " + uri);
                }
            }
        }
        log.info("DTOs size: " + foundPrices.getPrices().size());
        return foundPrices;
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

        days.forEach(d -> uriList.add(URI.create(
                String.format(
                        endpointProperties.getCoingecko().getBaseUrl() + endpointProperties.getCoingecko().getTokenPriceHistory(),
                        coingeckoId, d / 1000, (d + DAYS_IN_RANGE) / 1000))));

        return uriList;
    }

    private long calculateDaysBetweenStartDateAndNow() {
        LocalDateTime startDate = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(START_DATE_IN_MILLIS), ZoneId.systemDefault()).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.MILLIS.between(startDate, now);
    }

}
