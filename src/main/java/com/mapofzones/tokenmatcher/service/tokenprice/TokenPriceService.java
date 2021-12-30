package com.mapofzones.tokenmatcher.service.tokenprice;

import com.mapofzones.tokenmatcher.domain.Token;
import com.mapofzones.tokenmatcher.domain.TokenPrice;
import com.mapofzones.tokenmatcher.service.tokenprice.client.CoingeckoClient;
import com.mapofzones.tokenmatcher.service.tokenprice.client.dto.TokenPriceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class TokenPriceService implements ITokenPriceService {

    private final CoingeckoClient coingeckoClient;
    private final TokenPriceRepository tokenPriceRepository;

    public TokenPriceService(CoingeckoClient coingeckoClient,
                             TokenPriceRepository tokenPriceRepository) {
        this.coingeckoClient = coingeckoClient;
        this.tokenPriceRepository = tokenPriceRepository;
    }

    @Override
    public void findAndSaveTokenPriceByToken(Token token) {

        LocalDateTime lastTokenPriceTime = tokenPriceRepository.findLastTokenPriceByZone(token.getTokenId().getZone());

        TokenPriceDto tokenPriceDto = coingeckoClient.findTokenPrice(token.getCoingeckoId(), lastTokenPriceTime);
        List<TokenPrice> preparedTokenPriceList = new ArrayList<>();

        for (List<String> priceInConcreteHour : tokenPriceDto.getPrices()) {
            TokenPrice.TokenPriceId tokenPriceId = new TokenPrice.TokenPriceId(token.getTokenId().getZone(),
                    token.getTokenId().getBaseDenom(), millisToLocalDateTime(Long.parseLong(priceInConcreteHour.get(0))));

            TokenPrice tokenPrice = new TokenPrice(tokenPriceId);
            tokenPrice.setCoingeckoSymbolPriceInUsd(BigDecimal.valueOf(Double.parseDouble(priceInConcreteHour.get(1))));
            preparedTokenPriceList.add(tokenPrice);
        }
        log.info("Ready to save all preparedTokenPriceList, size: " + preparedTokenPriceList.size());
        tokenPriceRepository.saveAll(preparedTokenPriceList);
        log.info("Saved all preparedTokenPriceList, size: " + preparedTokenPriceList.size());
    }

    private LocalDateTime millisToLocalDateTime(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).truncatedTo(ChronoUnit.HOURS);
    }
}
