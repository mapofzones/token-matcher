package com.mapofzones.tokenmatcher.service.token;

import com.mapofzones.tokenmatcher.domain.Token;

import java.util.List;

public interface ITokenService {

    Token save(Token token);
    List<Token> FindAllByCoingeckoIsNotNull();

}
