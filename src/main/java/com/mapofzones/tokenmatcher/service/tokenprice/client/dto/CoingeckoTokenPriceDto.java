package com.mapofzones.tokenmatcher.service.tokenprice.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapofzones.tokenmatcher.utils.TimeHelper;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties({"market_caps", "total_volumes"})
public class CoingeckoTokenPriceDto {

    /*
        List<String> priceInConcreteHour - consist of 2 values
        priceInConcreteHour.get(0) - datetime
        priceInConcreteHour.get(1) - private
     */
    @JsonProperty("prices")
    private List<List<String>> prices = new ArrayList<>();

    public void addPricesRows(List<List<String>> newPricesRows) {
        prices.addAll(newPricesRows);
    }

    public LocalDateTime getLastPriceTime() {
        return prices.isEmpty() ? LocalDateTime.now() : TimeHelper.millisToLocalDateTime(Long.parseLong(prices.get(prices.size() - 1).get(0)));
    }
}
