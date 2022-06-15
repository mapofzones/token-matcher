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
public class CoingeckoTokenPriceDto extends AbstractDexTokenPriceDto {

    /*
        List<String> priceInConcreteHour - consist of 2 values
        priceInConcreteHour.get(0) - datetime
        priceInConcreteHour.get(1) - price
     */
    @JsonProperty("prices")
    private List<List<String>> prices = new ArrayList<>();

    @JsonProperty("market_caps")
    private List<List<String>> marketCups = new ArrayList<>();

    @JsonProperty("total_volumes")
    private List<List<String>> totalVolumes = new ArrayList<>();

    public void addPricesRows(List<List<String>> newPricesRows) {
        prices.addAll(newPricesRows);
    }

    public void addMarketCupsRows(List<List<String>> newMarketCupsRows) {
        marketCups.addAll(newMarketCupsRows);
    }

    public void addTotalVolumesRows(List<List<String>> newTotalVolumesRows) {
        totalVolumes.addAll(newTotalVolumesRows);
    }

    @Override
    public TokenPriceDto toTokenPrice() {

        System.out.println("price: " + prices.size());
        System.out.println("marketCaps: " + marketCups.size());
        System.out.println("totalVolumes: " + totalVolumes.size());

        List<TokenPriceDto.PriceInTime> priceInTimeList = new ArrayList<>();
        for (int i = 0; i < prices.size(); i++) {
            priceInTimeList.add(new TokenPriceDto.PriceInTime(
                    TimeHelper.millisToLocalDateTime(prices.get(i).get(0)),
                    BigDecimal.valueOf(Double.parseDouble(prices.get(i).get(1))),
                    BigDecimal.valueOf(Double.parseDouble(marketCups.get(i).get(1))),
                    BigDecimal.valueOf(Double.parseDouble(totalVolumes.get(i).get(1)))
            ));
        }
        return new TokenPriceDto(priceInTimeList);
    }
}
