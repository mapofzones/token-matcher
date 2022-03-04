package com.mapofzones.tokenmatcher.service.tokenprice.services;

import com.mapofzones.tokenmatcher.domain.token.Token;

public interface ITokenPriceService {

    void findAndSaveTokenPrice(Token token);

}
