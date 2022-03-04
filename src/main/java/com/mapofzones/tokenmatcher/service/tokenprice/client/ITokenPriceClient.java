package com.mapofzones.tokenmatcher.service.tokenprice.client;

import com.mapofzones.tokenmatcher.service.tokenprice.client.dto.AbstractDexTokenPriceDto;

import java.time.LocalDateTime;

public interface ITokenPriceClient {

    AbstractDexTokenPriceDto findTokenPrice(String tokenId, LocalDateTime lastTokenPriceTime);

}
