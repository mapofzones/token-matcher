package com.mapofzones.tokenmatcher.service.token;

import com.mapofzones.tokenmatcher.domain.Token;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TokenService implements ITokenService {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Token save(Token token) {
        return tokenRepository.save(token);
    }
}
