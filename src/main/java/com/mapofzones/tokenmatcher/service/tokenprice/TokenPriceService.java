package com.mapofzones.tokenmatcher.service.tokenprice;

import com.mapofzones.tokenmatcher.common.constants.DateConstants;
import com.mapofzones.tokenmatcher.domain.Token;
import com.mapofzones.tokenmatcher.domain.TokenPrice;
import com.mapofzones.tokenmatcher.service.tokenprice.client.CoingeckoClient;
import com.mapofzones.tokenmatcher.service.tokenprice.client.OsmosisClient;
import com.mapofzones.tokenmatcher.service.tokenprice.client.dto.CoingeckoTokenPriceDto;
import com.mapofzones.tokenmatcher.service.tokenprice.client.dto.OsmosisTokenPriceDto;
import com.mapofzones.tokenmatcher.utils.TimeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class TokenPriceService implements ITokenPriceService {

    private final CoingeckoClient coingeckoClient;
    private final OsmosisClient osmosisClient;
    private final TokenPriceRepository tokenPriceRepository;

    public TokenPriceService(CoingeckoClient coingeckoClient,
                             OsmosisClient osmosisClient,
                             TokenPriceRepository tokenPriceRepository) {
        this.coingeckoClient = coingeckoClient;
        this.osmosisClient = osmosisClient;
        this.tokenPriceRepository = tokenPriceRepository;
    }

    @Override
    public void findAndSaveTokenPriceByCoingeckoId(Token token) {

        Boolean isExistsPricesForCurrentToken = tokenPriceRepository.existsByCoingeckoSymbolPriceInUsdIsNotNullAndTokenPriceId_BaseDenom(token.getTokenId().getBaseDenom());
        List<TokenPrice> existsTokenPricesWithCoingeckoPriceIsNull;
        if (isExistsPricesForCurrentToken) {
            existsTokenPricesWithCoingeckoPriceIsNull = tokenPriceRepository.findAllAfterFirstByZoneAndCoingeckoPriceIsNull(token.getTokenId().getBaseDenom());
        } else existsTokenPricesWithCoingeckoPriceIsNull = tokenPriceRepository.findAllByZoneAndCoingeckoPriceIsNull(token.getTokenId().getBaseDenom());

        List<TokenPrice> preparedTokenPriceList = new ArrayList<>();

        if (existsTokenPricesWithCoingeckoPriceIsNull == null || existsTokenPricesWithCoingeckoPriceIsNull.isEmpty()) {
            LocalDateTime lastTokenPriceTime = tokenPriceRepository.findLastTimeOfTokenPriceByZone(token.getTokenId().getBaseDenom()).orElse(TimeHelper.millisToLocalDateTime(DateConstants.START_DATE_IN_MILLIS));
            CoingeckoTokenPriceDto tokenPriceDto = coingeckoClient.findTokenPrice(token.getCoingeckoId(), lastTokenPriceTime);

            if (!lastTokenPriceTime.isEqual(tokenPriceDto.getLastPriceTime())) {
                for (List<String> priceInConcreteHour : tokenPriceDto.getPrices()) {
                    preparedTokenPriceList.add(TokenPrice.tokenPriceFromCoingeckoPriceDto(priceInConcreteHour, token));
                }
            }
        } else {
            CoingeckoTokenPriceDto tokenPriceDto;
            if (isExistsPricesForCurrentToken) {
                LocalDateTime firstTokenPriceTimeWithCoingeckoIsNull = existsTokenPricesWithCoingeckoPriceIsNull.get(0).getTokenPriceId().getDatetime();
                tokenPriceDto = coingeckoClient.findTokenPrice(token.getCoingeckoId(), firstTokenPriceTimeWithCoingeckoIsNull);
            } else {
                tokenPriceDto = coingeckoClient.findTokenPrice(token.getCoingeckoId(), TimeHelper.millisToLocalDateTime(DateConstants.START_DATE_IN_MILLIS));
            }

            for (List<String> priceInConcreteHour : tokenPriceDto.getPrices()) {
                LocalDateTime currentConcreteTime = TimeHelper.millisToLocalDateTime(Long.parseLong(priceInConcreteHour.get(0)));
                for (TokenPrice tokenPrice : existsTokenPricesWithCoingeckoPriceIsNull) {
                    if (currentConcreteTime.isBefore(tokenPrice.getTokenPriceId().getDatetime()) && !isExistsPricesForCurrentToken) {
                        preparedTokenPriceList.add(TokenPrice.tokenPriceFromCoingeckoPriceDto(priceInConcreteHour, token));
                        break;
                    } else if (currentConcreteTime.isEqual(tokenPrice.getTokenPriceId().getDatetime())) {
                        tokenPrice.setCoingeckoSymbolPriceInUsd(BigDecimal.valueOf(Double.parseDouble(priceInConcreteHour.get(1))));
                        preparedTokenPriceList.add(tokenPrice);
                        break;
                    }
                }
            }
        }

        log.info("Ready to save all preparedTokenPriceList in CoingeckoSymbolPriceInUsd, size: " + preparedTokenPriceList.size());
        tokenPriceRepository.saveAll(preparedTokenPriceList);
        log.info("Saved all preparedTokenPriceList in CoingeckoSymbolPriceInUsd, size: " + preparedTokenPriceList.size());
    }

    @Override
    public void findAndSaveTokenPriceByOsmosisId(Token token) {

        Boolean isExistsPricesForCurrentToken = tokenPriceRepository.existsByOsmosisSymbolPriceInUsdIsNotNullAndTokenPriceId_BaseDenom(token.getTokenId().getBaseDenom());
        List<TokenPrice> existsTokenPricesWithOsmosisPriceIsNull;
        if (isExistsPricesForCurrentToken) {
            existsTokenPricesWithOsmosisPriceIsNull = tokenPriceRepository.findAllAfterFirstByZoneAndOsmosisPriceIsNull(token.getTokenId().getBaseDenom());
        } else existsTokenPricesWithOsmosisPriceIsNull = tokenPriceRepository.findAllByZoneAndOsmosisPriceIsNull(token.getTokenId().getBaseDenom());

        List<TokenPrice> preparedTokenPriceList = new ArrayList<>();

        if (existsTokenPricesWithOsmosisPriceIsNull == null || existsTokenPricesWithOsmosisPriceIsNull.isEmpty()) {
            LocalDateTime lastTokenPriceTime = tokenPriceRepository.findLastTimeOfTokenPriceByZone(token.getTokenId().getBaseDenom()).orElse(TimeHelper.millisToLocalDateTime(DateConstants.START_DATE_IN_MILLIS));
            OsmosisTokenPriceDto osmosisTokenPriceDto = osmosisClient.findTokenPrice(token.getOsmosisId(), lastTokenPriceTime);

            if (!lastTokenPriceTime.equals(osmosisTokenPriceDto.getLastPriceTime())) {
                for (OsmosisTokenPriceDto.OsmosisTokenPrice osmosisTokenPrice : osmosisTokenPriceDto.getOsmosisTokenPriceList()) {
                    preparedTokenPriceList.add(TokenPrice.tokenPriceFromOsmosisPriceDto(osmosisTokenPrice, token));
                }
            }
        } else {

            OsmosisTokenPriceDto osmosisTokenPriceDto;
            if (isExistsPricesForCurrentToken) {
                LocalDateTime firstTokenPriceTimeWithOsmosisIsNull = existsTokenPricesWithOsmosisPriceIsNull.get(0).getTokenPriceId().getDatetime();
                osmosisTokenPriceDto = osmosisClient.findTokenPrice(token.getOsmosisId(), firstTokenPriceTimeWithOsmosisIsNull);
            } else {
                osmosisTokenPriceDto = osmosisClient.findTokenPrice(token.getOsmosisId(), TimeHelper.millisToLocalDateTime(DateConstants.START_DATE_IN_MILLIS));
            }


            for (OsmosisTokenPriceDto.OsmosisTokenPrice osmosisTokenPrice : osmosisTokenPriceDto.getOsmosisTokenPriceList()) {
                LocalDateTime currentOsmosisTokenPriceTime = TimeHelper.millisToLocalDateTime(osmosisTokenPrice.getTime() * 1000);
                for (TokenPrice tokenPrice : existsTokenPricesWithOsmosisPriceIsNull) {
                    if (currentOsmosisTokenPriceTime.isBefore(tokenPrice.getTokenPriceId().getDatetime()) && !isExistsPricesForCurrentToken) {
                        preparedTokenPriceList.add(TokenPrice.tokenPriceFromOsmosisPriceDto(osmosisTokenPrice, token));
                        break;
                    } else if (currentOsmosisTokenPriceTime.isEqual(tokenPrice.getTokenPriceId().getDatetime())) {
                        tokenPrice.setOsmosisSymbolPriceInUsdFromDto(osmosisTokenPrice);
                        preparedTokenPriceList.add(tokenPrice);
                        break;
                    }
                }
            }
        }

        log.info("Ready to save all preparedTokenPriceList in OsmosisSymbolPriceInUsd, size: " + preparedTokenPriceList.size());
        tokenPriceRepository.saveAll(preparedTokenPriceList);
        log.info("Saved all preparedTokenPriceList in OsmosisSymbolPriceInUsd, size: " + preparedTokenPriceList.size());
    }
}
