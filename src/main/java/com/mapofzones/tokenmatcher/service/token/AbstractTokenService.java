package com.mapofzones.tokenmatcher.service.token;

import com.mapofzones.tokenmatcher.domain.token.Token;
import com.mapofzones.tokenmatcher.domain.token.TokenId;
import com.mapofzones.tokenmatcher.service.base.GenericService;

import java.util.List;

public abstract class AbstractTokenService extends GenericService<Token, TokenId, TokenRepository> implements ITokenService {

    private final TokenRepository tokenRepository;

    public AbstractTokenService(TokenRepository repository) {
        super(repository);
        this.tokenRepository = repository;
    }

    @Override
    public void saveToken(Token token) {
        tokenRepository.save(token.getTokenId().getZone(), token.getTokenId().getBaseDenom());
    }

    @Override
    public abstract List<Token> findAllByDexIdIsNotNull();
}
