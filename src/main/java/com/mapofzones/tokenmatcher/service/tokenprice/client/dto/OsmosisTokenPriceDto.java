package com.mapofzones.tokenmatcher.service.tokenprice.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices.dto.TokenPriceDto;
import com.mapofzones.tokenmatcher.utils.TimeHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
public class OsmosisTokenPriceDto extends AbstractDexTokenPriceDto {

    private List<OsmosisTokenPrice> osmosisTokenPriceList;

    public OsmosisTokenPriceDto(OsmosisTokenPrice[] osmosisTokenPriceArr) {
        this.osmosisTokenPriceList = Arrays.asList(Objects.requireNonNull(osmosisTokenPriceArr));
    }

    public OsmosisTokenPriceDto(List<OsmosisTokenPrice> osmosisTokenPriceList) {
        this.osmosisTokenPriceList = osmosisTokenPriceList;
    }

    @Override
    public TokenPriceDto toTokenPrice() {
        List<TokenPriceDto.PriceInTime> priceInTimeList = new ArrayList<>();
        osmosisTokenPriceList.forEach(osmosisTokenPrice -> priceInTimeList.add(new TokenPriceDto.PriceInTime(TimeHelper.millisToLocalDateTime(osmosisTokenPrice.getTime() * 1000), setOsmosisSymbolPriceInUsdFromDto(osmosisTokenPrice))));
        return new TokenPriceDto(priceInTimeList);
    }

    private BigDecimal setOsmosisSymbolPriceInUsdFromDto(OsmosisTokenPrice osmosisTokenPrice) {
        return osmosisTokenPrice.getClose().add(osmosisTokenPrice.getOpen()).add(osmosisTokenPrice.getHigh()).add(osmosisTokenPrice.getLow()).divide(BigDecimal.valueOf(4), RoundingMode.DOWN);
    }

    @Data
    public static class OsmosisTokenPrice {
        @JsonProperty("time")
        private Long time;
        @JsonProperty("close")
        private BigDecimal close;
        @JsonProperty("high")
        private BigDecimal high;
        @JsonProperty("low")
        private BigDecimal low;
        @JsonProperty("open")
        private BigDecimal open;
        @JsonProperty("volume")
        private BigDecimal volume;
    }

}
