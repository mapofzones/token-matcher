package com.mapofzones.tokenmatcher.domain.tokenprice;

import com.mapofzones.tokenmatcher.domain.token.Token;
import com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices.dto.TokenPriceDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class TokenPrice<E extends TokenPrice<E>> {

    @EmbeddedId
    private TokenPriceId tokenPriceId;

    @Column(name = "COINGECKO_SYMBOL_PRICE_IN_USD")
    private BigDecimal coingeckoSymbolPriceInUsd;

    @Column(name = "OSMOSIS_SYMBOL_PRICE_IN_USD")
    private BigDecimal osmosisSymbolPriceInUsd;

    @Column(name = "COINGECKO_SYMBOL_MARKET_CUP_IN_USD")
    private BigDecimal coingeckoSymbolMarketCupInUsd;

    @Column(name = "COINGECKO_SYMBOL_TOTAL_VOLUMES_IN_USD")
    private BigDecimal coingeckoSymbolTotalVolumesInUsd;

    @Transient
    private E concreteTokenPrice;

    public TokenPrice(TokenPriceId tokenPriceId) {
        this.tokenPriceId = tokenPriceId;
    }

    public TokenPrice(TokenPriceId tokenPriceId, E concreteTokenPrice) {
        this.tokenPriceId = tokenPriceId;
        this.concreteTokenPrice = concreteTokenPrice;
    }

    public abstract E tokenPriceFromPriceInTime(TokenPriceDto.PriceInTime priceInTime, Token token);

    public abstract void setPriceInUsd(BigDecimal tokenPrice, BigDecimal marketCup, BigDecimal totalVolumes);

    public abstract String getDexTokenId(Token token);

}
