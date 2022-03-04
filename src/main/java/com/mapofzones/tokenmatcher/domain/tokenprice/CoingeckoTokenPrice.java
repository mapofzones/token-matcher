package com.mapofzones.tokenmatcher.domain.tokenprice;

import com.mapofzones.tokenmatcher.domain.token.Token;
import com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices.dto.TokenPriceDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Component
@Entity
@NoArgsConstructor
@Table(name = "TOKEN_PRICES")
public class CoingeckoTokenPrice extends TokenPrice<CoingeckoTokenPrice> {

    public CoingeckoTokenPrice(TokenPriceId tokenPriceId) {
        super(tokenPriceId);
    }

    public CoingeckoTokenPrice(TokenPriceId tokenPriceId, CoingeckoTokenPrice concreteTokenPrice) {
        super(tokenPriceId, concreteTokenPrice);
    }

    @Override
    public CoingeckoTokenPrice tokenPriceFromPriceInTime(TokenPriceDto.PriceInTime priceInTime, Token token) {
        TokenPriceId tokenPriceId = new TokenPriceId(token.getTokenId().getZone(),
                token.getTokenId().getBaseDenom(), priceInTime.getTime());

        CoingeckoTokenPrice tokenPrice = new CoingeckoTokenPrice(tokenPriceId);
        tokenPrice.setCoingeckoSymbolPriceInUsd(priceInTime.getPrice());
        return tokenPrice;
    }

    @Override
    public void setPriceInUsd(BigDecimal tokenPrice) {
        this.setCoingeckoSymbolPriceInUsd(tokenPrice);
    }

    @Override
    public String getDexTokenId(Token token) {
        return token.getCoingeckoId();
    }
}
