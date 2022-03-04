package com.mapofzones.tokenmatcher.service.token;

import com.mapofzones.tokenmatcher.domain.token.Token;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OsmosisTokenService extends AbstractTokenService implements ITokenService {

    private final TokenRepository tokenRepository;

    public OsmosisTokenService(TokenRepository tokenRepository) {
        super(tokenRepository);
        this.tokenRepository = tokenRepository;
    }

    @Override
    public List<Token> findAllByDexIdIsNotNull() {
        return tokenRepository.findAllByOsmosisIdIsNotNull();
    }
}
