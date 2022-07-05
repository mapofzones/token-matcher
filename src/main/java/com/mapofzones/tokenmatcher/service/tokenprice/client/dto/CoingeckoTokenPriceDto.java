package com.mapofzones.tokenmatcher.service.tokenprice.client.dto;

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
public class CoingeckoTokenPriceDto extends AbstractDexTokenPriceDto {

    /*
        List<String> priceInConcreteHour - consist of 2 values
        priceInConcreteHour.get(0) - datetime
        priceInConcreteHour.get(1) - price
     */
    @JsonProperty("prices")
    private List<List<String>> prices = new ArrayList<>();

    @JsonProperty("market_caps")
    private List<List<String>> marketCaps = new ArrayList<>();

    @JsonProperty("total_volumes")
    private List<List<String>> totalVolumes = new ArrayList<>();

    public void addPricesRows(List<List<String>> newPricesRows) {
        prices.addAll(newPricesRows);
    }

    public void addMarketCapsRows(List<List<String>> newMarketCapsRows) {
        marketCaps.addAll(newMarketCapsRows);
    }

    public void addTotalVolumesRows(List<List<String>> newTotalVolumesRows) {
        totalVolumes.addAll(newTotalVolumesRows);
    }

    @Override
    public TokenPriceDto toTokenPrice() {

        List<TokenPriceDto.PriceInTime> priceInTimeList = new ArrayList<>();
        for (int i = 0; i < prices.size(); i++) {
            priceInTimeList.add(new TokenPriceDto.PriceInTime(
                    TimeHelper.millisToLocalDateTime(prices.get(i).get(0)),
                    new BigDecimal(prices.get(i).get(1)),
                    new BigDecimal(marketCaps.get(i).get(1)),
                    new BigDecimal(totalVolumes.get(i).get(1))
            ));
        }
        return new TokenPriceDto(priceInTimeList);
    }
}
