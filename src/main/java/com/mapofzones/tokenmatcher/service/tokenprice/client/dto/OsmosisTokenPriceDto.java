package com.mapofzones.tokenmatcher.service.tokenprice.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapofzones.tokenmatcher.utils.TimeHelper;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
public class OsmosisTokenPriceDto {

    private List<OsmosisTokenPrice> osmosisTokenPriceList;

    public OsmosisTokenPriceDto(OsmosisTokenPrice[] osmosisTokenPriceArr) {
        this.osmosisTokenPriceList = Arrays.asList(Objects.requireNonNull(osmosisTokenPriceArr));
    }

    public LocalDateTime getLastPriceTime() {
        return osmosisTokenPriceList.isEmpty() ? LocalDateTime.now() : TimeHelper.millisToLocalDateTime(osmosisTokenPriceList.get(osmosisTokenPriceList.size() - 1).getTime() * 1000);
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
    }

}
