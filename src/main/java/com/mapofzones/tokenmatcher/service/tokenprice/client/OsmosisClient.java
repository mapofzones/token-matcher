package com.mapofzones.tokenmatcher.service.tokenprice.client;

import com.mapofzones.tokenmatcher.common.properties.EndpointProperties;
import com.mapofzones.tokenmatcher.service.tokenprice.client.dto.OsmosisTokenPriceDto;
import com.mapofzones.tokenmatcher.utils.TimeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.mapofzones.tokenmatcher.common.constants.DateConstants.MILLIS_IN_DAY;

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

        URI uri60 = URI.create(String.format(endpointProperties.getOsmosis().getBaseUrl() + endpointProperties.getOsmosis().getTokenPriceHistoryV2(), osmosisId, 60));
        URI uri1440 = URI.create(String.format(endpointProperties.getOsmosis().getBaseUrl() + endpointProperties.getOsmosis().getTokenPriceHistoryV2(), osmosisId, 1440));

        List<OsmosisTokenPriceDto.OsmosisTokenPrice> responsePriceList = Arrays.asList(doRequest(uri1440));
        List<OsmosisTokenPriceDto.OsmosisTokenPrice> responsePriceList1 = responsePriceList.stream().filter(prices ->
                TimeHelper.millisToLocalDateTime(prices.getTime() * 1000).isBefore(LocalDateTime.now().minus(MILLIS_IN_DAY * 10, ChronoUnit.MILLIS))).collect(Collectors.toList());

        responsePriceList1.addAll(Arrays.asList(doRequest(uri60)));
        return new OsmosisTokenPriceDto(responsePriceList1);
    }


    private OsmosisTokenPriceDto.OsmosisTokenPrice[] doRequest(URI uri) {
        try {
            ResponseEntity<OsmosisTokenPriceDto.OsmosisTokenPrice[]> response = tokenPriceRestTemplate.getForEntity(uri, OsmosisTokenPriceDto.OsmosisTokenPrice[].class);
            log.info("Success! Osmosis URI: " + uri);
            return response.getBody();
        } catch (RestClientException | NullPointerException e) {
            log.warn("(OsmosisClient)Request cant be completed. " + uri);
            return new OsmosisTokenPriceDto.OsmosisTokenPrice[0];
        }
    }
}
