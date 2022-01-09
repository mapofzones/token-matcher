package com.mapofzones.tokenmatcher.service.token;

import com.mapofzones.tokenmatcher.domain.Token;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TokenService implements ITokenService {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void save(Token token) {
        tokenRepository.save(token.getTokenId().getZone(), token.getTokenId().getBaseDenom());
    }

    @Override
    public List<Token> findAllByCoingeckoIsNotNull() {
        return tokenRepository.findAllByCoingeckoIdIsNotNull();
    }

    @Override
    public List<Token> findAllByOsmosisIsNotNull() {
        return tokenRepository.findAllByOsmosisIdIsNotNull();
    }
}
