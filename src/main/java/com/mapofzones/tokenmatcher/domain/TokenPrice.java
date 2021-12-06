package com.mapofzones.tokenmatcher.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TOKEN_PRICES")
public class TokenPrice {

    public TokenPrice(TokenPriceId tokenPriceId) {
        this.tokenPriceId = tokenPriceId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class TokenPriceId implements Serializable {

        @Column(name = "ZONE")
        private String zone;

        @Column(name = "BASE_DENOM")
        private String baseDenom;

        @Column(name = "DATETIME")
        private LocalDateTime datetime;
    }

    @EmbeddedId
    private TokenPriceId tokenPriceId;

    @Column(name = "COINGECKO_SYMBOL_PRICE_IN_USD")
    private BigDecimal coingeckoSymbolPriceInUsd;
}
