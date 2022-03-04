package com.mapofzones.tokenmatcher.service.tokenprice.client.dto;

import com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices.dto.TokenPriceDto;

public abstract class AbstractDexTokenPriceDto {

    public abstract TokenPriceDto toTokenPrice();

}
