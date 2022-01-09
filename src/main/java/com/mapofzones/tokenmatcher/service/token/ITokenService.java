package com.mapofzones.tokenmatcher.service.token;

import com.mapofzones.tokenmatcher.domain.Token;

import java.util.List;

public interface ITokenService {

    void save(Token token);
    List<Token> findAllByCoingeckoIsNotNull();
    List<Token> findAllByOsmosisIsNotNull();

}
