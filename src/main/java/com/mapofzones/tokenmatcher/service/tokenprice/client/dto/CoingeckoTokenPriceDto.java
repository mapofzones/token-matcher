package com.mapofzones.tokenmatcher.service.tokenprice.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices.dto.TokenPriceDto;
import com.mapofzones.tokenmatcher.utils.TimeHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"market_caps", "total_volumes"})
public class CoingeckoTokenPriceDto extends AbstractDexTokenPriceDto {

    /*
        List<String> priceInConcreteHour - consist of 2 values
        priceInConcreteHour.get(0) - datetime
        priceInConcreteHour.get(1) - price
     */
    @JsonProperty("prices")
    private List<List<String>> prices = new ArrayList<>();

    public void addPricesRows(List<List<String>> newPricesRows) {
        prices.addAll(newPricesRows);
    }

    @Override
    public TokenPriceDto toTokenPrice() {
        List<TokenPriceDto.PriceInTime> priceInTimeList = new ArrayList<>();
        prices.forEach(price -> priceInTimeList.add(new TokenPriceDto.PriceInTime(TimeHelper.millisToLocalDateTime(price.get(0)), BigDecimal.valueOf(Double.parseDouble(price.get(1))))));
        return new TokenPriceDto(priceInTimeList);
    }
}
