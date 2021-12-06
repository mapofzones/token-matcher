package com.mapofzones.tokenmatcher.service.tokenprice;

import com.mapofzones.tokenmatcher.domain.Token;
import com.mapofzones.tokenmatcher.domain.TokenPrice;
import com.mapofzones.tokenmatcher.service.tokenprice.client.CoingeckoClient;
import com.mapofzones.tokenmatcher.service.tokenprice.client.dto.TokenPriceInSpecificHourDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TokenPriceService implements ITokenPriceService {

    // 2021-01-01
    private final static Long START_DATE_IN_MILLIS = 1609448400000L;

    private final CoingeckoClient coingeckoClient;
    private final TokenPriceRepository tokenPriceRepository;

    public TokenPriceService(CoingeckoClient coingeckoClient,
                             TokenPriceRepository tokenPriceRepository) {
        this.coingeckoClient = coingeckoClient;
        this.tokenPriceRepository = tokenPriceRepository;
    }

    @Override
    public void findAndSaveTokenPriceByToken(Token token) {
        List<TokenPriceInSpecificHourDto> dtoList = coingeckoClient.findTokenPrice(token.getCoingeckoId(), calculateDaysBetweenStartDateAndNow());

        List<TokenPrice> preparedTokenPriceList = new ArrayList<>();

        for (TokenPriceInSpecificHourDto dto : dtoList) {
            TokenPrice.TokenPriceId tokenPriceId = new TokenPrice.TokenPriceId(token.getTokenId().getZone(),
                    token.getTokenId().getBaseDenom(), millisToLocalDateTime(dto.getTimeInMillis()));

            TokenPrice tokenPrice = new TokenPrice(tokenPriceId);
            tokenPrice.setCoingeckoSymbolPriceInUsd(dto.getPrice());
            preparedTokenPriceList.add(tokenPrice);
        }

        tokenPriceRepository.saveAll(preparedTokenPriceList);
    }

    private long calculateDaysBetweenStartDateAndNow() {
        LocalDateTime startDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(START_DATE_IN_MILLIS), ZoneId.systemDefault()).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.DAYS.between(startDate, now);
    }

    private LocalDateTime millisToLocalDateTime(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).truncatedTo(ChronoUnit.HOURS);
    }
}
