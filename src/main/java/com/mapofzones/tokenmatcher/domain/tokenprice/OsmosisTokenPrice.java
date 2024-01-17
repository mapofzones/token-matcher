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
public class OsmosisTokenPrice extends TokenPrice<OsmosisTokenPrice> {

    public OsmosisTokenPrice(TokenPriceId tokenPriceId) {
        super(tokenPriceId);
    }

    public OsmosisTokenPrice(TokenPriceId tokenPriceId, OsmosisTokenPrice concreteTokenPrice) {
        super(tokenPriceId, concreteTokenPrice);
    }

    @Override
    public OsmosisTokenPrice tokenPriceFromPriceInTime(TokenPriceDto.PriceInTime priceInTime, Token token) {
        TokenPriceId tokenPriceId = new TokenPriceId(token.getTokenId().getZone(),
                token.getTokenId().getBaseDenom(), priceInTime.getTime());

        OsmosisTokenPrice tokenPrice = new OsmosisTokenPrice(tokenPriceId);
        tokenPrice.setOsmosisSymbolPriceInUsd(priceInTime.getPrice());
        tokenPrice.setOsmosisSymbolTotalVolumesInUsd(priceInTime.getTotalVolumes());
        return tokenPrice;
    }

    @Override
    public void setPriceInUsd(BigDecimal tokenPrice, BigDecimal marketCap, BigDecimal totalVolumes) {
        this.setOsmosisSymbolPriceInUsd(tokenPrice);
        this.setOsmosisSymbolTotalVolumesInUsd(totalVolumes);
    }

    @Override
    public String getDexTokenId(Token token) {
        return token.getOsmosisId();
    }
}
