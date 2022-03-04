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

    private List<PriceInTime> priceInTimeList;

    public LocalDateTime getLastPriceTime() {
        return priceInTimeList.isEmpty() ? LocalDateTime.now() : priceInTimeList.get(priceInTimeList.size() - 1).getTime();
    }

    public LocalDateTime getFirstPriceTime() {
        return priceInTimeList.isEmpty() ? LocalDateTime.now() : priceInTimeList.get(0).getTime();
    }

    @Data
    public static class PriceInTime {

        public PriceInTime(LocalDateTime time, BigDecimal price) {
            this.time = time;
            this.price = price;
        }

        private LocalDateTime time;
        private BigDecimal price;
    }
}