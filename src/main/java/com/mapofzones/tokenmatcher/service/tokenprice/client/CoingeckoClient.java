package com.mapofzones.tokenmatcher.service.tokenprice.client;

import com.mapofzones.tokenmatcher.common.properties.EndpointProperties;
import com.mapofzones.tokenmatcher.service.tokenprice.client.dto.CoingeckoTokenPriceDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mapofzones.tokenmatcher.common.constants.DateConstants.DAYS_IN_RANGE;
import static com.mapofzones.tokenmatcher.common.constants.DateConstants.MILLIS_IN_DAY;
import static com.mapofzones.tokenmatcher.common.constants.DateConstants.START_DATE_IN_MILLIS;

@Slf4j
public class CoingeckoClient implements ITokenPriceClient {

    private final RestTemplate tokenPriceRestTemplate;
    private final EndpointProperties endpointProperties;

    public CoingeckoClient(RestTemplate tokenPriceRestTemplate,
                           EndpointProperties endpointProperties) {
        this.tokenPriceRestTemplate = tokenPriceRestTemplate;
        this.endpointProperties = endpointProperties;
    }

    public CoingeckoTokenPriceDto findTokenPrice(String coingeckoId, @NonNull LocalDateTime lastTokenPriceTime) {

        CoingeckoTokenPriceDto foundPrices = new CoingeckoTokenPriceDto();

        if (coingeckoId == null) {
            return foundPrices;
        }

        List<URI> uriList = prepareURLForFindPartsOfRange(coingeckoId, lastTokenPriceTime);
        log.debug("coingecko URIs size: " + uriList.size());
        if (!uriList.isEmpty()) {
            for (URI uri : uriList) {
                log.debug("coingecko URI: " + uri.toString());
                try {
                    ResponseEntity<CoingeckoTokenPriceDto> response = tokenPriceRestTemplate.getForEntity(uri, CoingeckoTokenPriceDto.class);
                    foundPrices.addPricesRows(Objects.requireNonNull(response.getBody()).getPrices());
                    foundPrices.addMarketCapsRows(Objects.requireNonNull(response.getBody()).getMarketCaps());
                    foundPrices.addTotalVolumesRows(Objects.requireNonNull(response.getBody()).getTotalVolumes());
                } catch (RestClientException e) {
                    log.debug("(CoingeckoClient)Request cant be completed. " + uri);
                }
            }
        }
        log.debug("DTOs size: " + foundPrices.getPrices().size());
        return foundPrices;
    }

    private List<URI> prepareURLForFindPartsOfRange(String coingeckoId, LocalDateTime lastTokenPriceTime) {

        List<URI> uriList = new ArrayList<>();

        Long startDate;

        List<Long> days = new ArrayList<>();
        if (lastTokenPriceTime == null) {
            startDate = START_DATE_IN_MILLIS;
        }
        else startDate = lastTokenPriceTime.minusDays(2).toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();

        days.add(startDate);

        Long allDaysInMillis = calculateDaysBetweenStartDateAndNow(startDate);

        Long countMillis = startDate;
        for (int i = 0; i < allDaysInMillis / DAYS_IN_RANGE; i++) {
            countMillis = countMillis + DAYS_IN_RANGE;
            days.add(countMillis);
        }

        for (Long day : days) {

            long untilDay = day + DAYS_IN_RANGE;

            if (untilDay > System.currentTimeMillis())
                untilDay = System.currentTimeMillis();

            if (day > untilDay - MILLIS_IN_DAY)
                day = untilDay - MILLIS_IN_DAY;


            String uriString;
            if (endpointProperties.getCoingecko().getProApiKey().equals("null")) {
                uriString = endpointProperties.getCoingecko().getBaseUrl() + endpointProperties.getCoingecko().getTokenPriceHistory();
            } else {
                uriString = endpointProperties.getCoingecko().getProBaseUrl() + endpointProperties.getCoingecko().getProTokenPriceHistory() + endpointProperties.getCoingecko().getProApiKey();
            }

            URI uri = URI.create(String.format(uriString, coingeckoId, day / 1000 + 1, untilDay / 1000));

            uriList.add(uri);
        }

        return uriList;
    }

    private long calculateDaysBetweenStartDateAndNow(Long start) {
        LocalDateTime startDate = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault()).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.MILLIS.between(startDate, now);
    }

}
