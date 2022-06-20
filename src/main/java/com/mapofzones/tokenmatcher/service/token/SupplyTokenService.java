package com.mapofzones.tokenmatcher.service.token;

import com.mapofzones.tokenmatcher.domain.token.Token;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SupplyTokenService extends AbstractTokenService implements ITokenService {

    private final TokenRepository tokenRepository;

    public SupplyTokenService(TokenRepository tokenRepository) {
        super(tokenRepository);
        this.tokenRepository = tokenRepository;
    }

    @Override
    public List<Token> findAllByDexIdIsNotNull() {
        return tokenRepository.findAllByTokenId_BaseDenomIsNotNull();
    }
}
