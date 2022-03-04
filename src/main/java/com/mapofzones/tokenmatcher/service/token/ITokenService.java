package com.mapofzones.tokenmatcher.service.token;

import com.mapofzones.tokenmatcher.domain.token.Token;
import com.mapofzones.tokenmatcher.domain.token.TokenId;
import com.mapofzones.tokenmatcher.service.base.IGenericService;

import java.util.List;

public interface ITokenService extends IGenericService<Token, TokenId, TokenRepository> {

    void saveToken(Token token);
    List<Token> findAllByDexIdIsNotNull();

}
