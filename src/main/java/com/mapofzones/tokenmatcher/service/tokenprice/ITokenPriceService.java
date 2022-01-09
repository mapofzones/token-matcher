package com.mapofzones.tokenmatcher.service.tokenprice;

import com.mapofzones.tokenmatcher.domain.Token;

public interface ITokenPriceService {

    void findAndSaveTokenPriceByCoingeckoId(Token token);
    void findAndSaveTokenPriceByOsmosisId(Token token);

}
