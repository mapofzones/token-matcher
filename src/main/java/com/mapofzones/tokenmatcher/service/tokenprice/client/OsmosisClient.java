package com.mapofzones.tokenmatcher.service.tokenprice.client;

import com.mapofzones.tokenmatcher.common.properties.EndpointProperties;
import com.mapofzones.tokenmatcher.service.tokenprice.client.dto.OsmosisTokenPriceDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static com.mapofzones.tokenmatcher.common.constants.DateConstants.MILLIS_IN_DAY;
import static com.mapofzones.tokenmatcher.common.constants.DateConstants.START_DATE_IN_MILLIS;

@Slf4j
public class OsmosisClient implements ITokenPriceClient {

    private final RestTemplate tokenPriceRestTemplate;
    private final EndpointProperties endpointProperties;

    public OsmosisClient(RestTemplate tokenPriceRestTemplate,
                         EndpointProperties endpointProperties) {
        this.tokenPriceRestTemplate = tokenPriceRestTemplate;
        this.endpointProperties = endpointProperties;
    }

    public OsmosisTokenPriceDto findTokenPrice(String osmosisId, LocalDateTime lastTokenPriceTime) {

        if (osmosisId == null) {
            log.warn("Osmosis token id is null");
            return new OsmosisTokenPriceDto(new OsmosisTokenPriceDto.OsmosisTokenPrice[0]);
        }

        long startDate;
        if (lastTokenPriceTime == null)
            startDate = START_DATE_IN_MILLIS;
        else startDate = lastTokenPriceTime.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();

        long allDays = calculateDaysBetweenStartDateAndNow(startDate) / MILLIS_IN_DAY + 1;

        URI uri = URI.create(String.format(endpointProperties.getOsmosis().getBaseUrl() + endpointProperties.getOsmosis().getTokenPriceHistory(), osmosisId, allDays + "d"));

        try {
            ResponseEntity<OsmosisTokenPriceDto.OsmosisTokenPrice[]> response = tokenPriceRestTemplate.getForEntity(uri, OsmosisTokenPriceDto.OsmosisTokenPrice[].class);
            log.info("Success! Osmosis URI: " + uri);
            return new OsmosisTokenPriceDto(response.getBody());
        } catch (RestClientException e) {
            log.warn("(OsmosisClient)Request cant be completed. " + uri);
        }
        return new OsmosisTokenPriceDto(new OsmosisTokenPriceDto.OsmosisTokenPrice[0]);
    }

    private long calculateDaysBetweenStartDateAndNow(Long start) {
        LocalDateTime startDate = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault()).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.MILLIS.between(startDate, now);
    }
}
