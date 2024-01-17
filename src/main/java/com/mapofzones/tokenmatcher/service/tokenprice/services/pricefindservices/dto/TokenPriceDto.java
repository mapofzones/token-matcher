package com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class TokenPriceDto {

    public TokenPriceDto(List<PriceInTime> priceInTimeList) {
        this.priceInTimeList = priceInTimeList;
    }

    public TokenPriceDto(List<PriceInTime> priceInTimeList, List<List<String>> extraParameters) {
        this.priceInTimeList = priceInTimeList;
        this.extraParameters = extraParameters;
    }

    private List<PriceInTime> priceInTimeList;

    private List<List<String>> extraParameters;

    public LocalDateTime getLastPriceTime() {
        return priceInTimeList.isEmpty() ? LocalDateTime.now() : priceInTimeList.get(priceInTimeList.size() - 1).getTime();
    }

    public LocalDateTime getFirstPriceTime() {
        return priceInTimeList.isEmpty() ? LocalDateTime.now() : priceInTimeList.get(0).getTime();
    }

    @Data
    public static class PriceInTime implements Cloneable {

        // TODO: May be change MarketCap and TotalVolumes to Abstract parameters

        private LocalDateTime time;
        private BigDecimal price;
        private BigDecimal marketCap;
        private BigDecimal totalVolumes;

        public PriceInTime(LocalDateTime time, BigDecimal price) {
            this.time = time;
            this.price = price;
        }

        public PriceInTime(LocalDateTime time, BigDecimal price, BigDecimal totalVolumes) {
            this.time = time;
            this.price = price;
            this.totalVolumes = totalVolumes;
        }

        public PriceInTime(LocalDateTime time, BigDecimal price, BigDecimal marketCap, BigDecimal totalVolumes) {
            this.time = time;
            this.price = price;
            this.marketCap = marketCap;
            this.totalVolumes = totalVolumes;
        }

        @Override
        public PriceInTime clone() {
            try {
                return (PriceInTime) super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }
}