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
public class TokenSupply extends TokenPrice<TokenSupply> {

    public TokenSupply(TokenPriceId tokenPriceId) {
        super(tokenPriceId);
    }

    public TokenSupply(TokenPriceId tokenPriceId, TokenSupply concreteTokenPrice) {
        super(tokenPriceId, concreteTokenPrice);
    }

    @Override
    public TokenSupply tokenPriceFromPriceInTime(TokenPriceDto.PriceInTime priceInTime, Token token) {
        TokenPriceId tokenPriceId = new TokenPriceId(token.getTokenId().getZone(),
                token.getTokenId().getBaseDenom(), priceInTime.getTime());

        TokenSupply tokenPrice = new TokenSupply(tokenPriceId);
        tokenPrice.setSymbolSupply(priceInTime.getPrice());
        return tokenPrice;
    }

    @Override
    public void setPriceInUsd(BigDecimal tokenPrice, BigDecimal marketCap, BigDecimal totalVolumes) {
        this.setSymbolSupply(tokenPrice);
    }

    @Override
    public String getDexTokenId(Token token) {
        return token.getTokenId().getZone() + ":" + token.getTokenId().getBaseDenom();
    }
}
