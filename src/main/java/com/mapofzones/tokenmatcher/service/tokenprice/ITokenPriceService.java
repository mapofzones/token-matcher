package com.mapofzones.tokenmatcher.service.tokenprice;

import com.mapofzones.tokenmatcher.domain.Token;

public interface ITokenPriceService {

    void findAndSaveTokenPriceByToken(Token token);

}
