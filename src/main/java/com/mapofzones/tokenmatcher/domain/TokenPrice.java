package com.mapofzones.tokenmatcher.domain;

import com.mapofzones.tokenmatcher.service.tokenprice.client.dto.OsmosisTokenPriceDto;
import com.mapofzones.tokenmatcher.utils.TimeHelper;
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
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

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

    public static TokenPrice tokenPriceFromCoingeckoPriceDto(List<String> priceInConcreteHour, Token token) {
        TokenPrice.TokenPriceId tokenPriceId = new TokenPrice.TokenPriceId(token.getTokenId().getZone(),
                token.getTokenId().getBaseDenom(), TimeHelper.millisToLocalDateTime(Long.parseLong(priceInConcreteHour.get(0))));

        TokenPrice tokenPrice = new TokenPrice(tokenPriceId);
        tokenPrice.setCoingeckoSymbolPriceInUsd(BigDecimal.valueOf(Double.parseDouble(priceInConcreteHour.get(1))));
        return tokenPrice;
    }

    public static TokenPrice tokenPriceFromOsmosisPriceDto(OsmosisTokenPriceDto.OsmosisTokenPrice osmosisTokenPrice, Token token) {
        TokenPrice.TokenPriceId tokenPriceId = new TokenPrice.TokenPriceId(token.getTokenId().getZone(),
                token.getTokenId().getBaseDenom(), TimeHelper.millisToLocalDateTime(osmosisTokenPrice.getTime() * 1000));

        TokenPrice tokenPrice = new TokenPrice(tokenPriceId);
        tokenPrice.setOsmosisSymbolPriceInUsdFromDto(osmosisTokenPrice);
        return tokenPrice;
    }

    @EmbeddedId
    private TokenPriceId tokenPriceId;

    @Column(name = "COINGECKO_SYMBOL_PRICE_IN_USD")
    private BigDecimal coingeckoSymbolPriceInUsd;

    @Column(name = "OSMOSIS_SYMBOL_PRICE_IN_USD")
    private BigDecimal osmosisSymbolPriceInUsd;

    public void setOsmosisSymbolPriceInUsdFromDto(OsmosisTokenPriceDto.OsmosisTokenPrice osmosisTokenPrice) {
        this.osmosisSymbolPriceInUsd = osmosisTokenPrice.getClose().add(osmosisTokenPrice.getOpen()).add(osmosisTokenPrice.getHigh()).add(osmosisTokenPrice.getLow()).divide(BigDecimal.valueOf(4), RoundingMode.DOWN);
    }
}
