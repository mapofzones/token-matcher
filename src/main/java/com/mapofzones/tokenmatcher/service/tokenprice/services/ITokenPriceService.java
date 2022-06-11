package com.mapofzones.tokenmatcher.service.tokenprice.services;

import com.mapofzones.tokenmatcher.domain.token.Token;

public interface ITokenPriceService {

    Integer findAndSaveTokenPrice(Token token);

}
