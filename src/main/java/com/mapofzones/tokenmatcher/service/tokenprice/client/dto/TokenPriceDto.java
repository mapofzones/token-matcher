package com.mapofzones.tokenmatcher.service.tokenprice.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties({"market_caps", "total_volumes"})
public class TokenPriceDto {
    @JsonProperty("prices")
    private List<List<String>> prices = new ArrayList<>();

    public void addPricesRows(List<List<String>> newPricesRows) {
        prices.addAll(newPricesRows);
    }
}
