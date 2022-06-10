package com.mapofzones.tokenmatcher.service.token;

import com.mapofzones.tokenmatcher.domain.token.Token;
import com.mapofzones.tokenmatcher.domain.token.TokenId;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Primary
@Service
@Transactional
public class CoingeckoTokenService extends AbstractTokenService implements ITokenService {

    private final TokenRepository tokenRepository;

    public CoingeckoTokenService(TokenRepository tokenRepository) {
        super(tokenRepository);
        this.tokenRepository = tokenRepository;
    }

    @Override
    public List<Token> findAllByDexIdIsNotNull() {
        return tokenRepository.findAllByCoingeckoIdIsNotNullOrderByPriceLastCheckedAtAsc();
    }
}
