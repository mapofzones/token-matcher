package com.mapofzones.tokenmatcher.service.tokenprice.services;

import com.mapofzones.tokenmatcher.common.constants.DateConstants;
import com.mapofzones.tokenmatcher.domain.token.Token;
import com.mapofzones.tokenmatcher.domain.tokenprice.TokenPrice;
import com.mapofzones.tokenmatcher.domain.tokenprice.TokenPriceId;
import com.mapofzones.tokenmatcher.service.tokenprice.repository.TokenPriceRepository;
import com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices.AbstractPriceFindService;
import com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices.dto.TokenPriceDto;
import com.mapofzones.tokenmatcher.utils.TimeHelper;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractTokenPriceService<S extends AbstractPriceFindService, R extends TokenPriceRepository<E, TokenPriceId>, E extends TokenPrice<E>> implements ITokenPriceService {

    private final S priceFindService;
    private final R repository;
    private final E tokenPriceHelper;

    public AbstractTokenPriceService(S priceFindService, R repository, E tokenPriceHelper) {
        this.priceFindService = priceFindService;
        this.repository = repository;
        this.tokenPriceHelper = tokenPriceHelper;
    }

    @Override
    public Integer findAndSaveTokenPrice(Token token) {
        Boolean isExistsPricesForCurrentToken = repository.existsByDexSymbolPriceInUsdIsNotNullAndTokenPriceId_BaseDenom(token.getTokenId().getBaseDenom(), token.getTokenId().getZone());
        List<E> existsTokenPricesWithPriceIsNull;

        if (isExistsPricesForCurrentToken) {
            existsTokenPricesWithPriceIsNull = repository.findAllAfterFirstByBaseDenomAndZoneAndDexPriceIsNull(token.getTokenId().getBaseDenom(), token.getTokenId().getZone());
        } else existsTokenPricesWithPriceIsNull = repository.findAllByBaseDenomAndZoneAndDexPriceIsNull(token.getTokenId().getBaseDenom(), token.getTokenId().getZone());

        List<E> preparedTokenPriceList = new ArrayList<>();

        if (existsTokenPricesWithPriceIsNull == null || existsTokenPricesWithPriceIsNull.isEmpty()) {
            LocalDateTime lastTokenPriceTime = repository.findLastTimeOfTokenPriceByBaseDenomAndZone(token.getTokenId().getBaseDenom(), token.getTokenId().getZone()).orElse(TimeHelper.millisToLocalDateTime(DateConstants.START_DATE_IN_MILLIS));
            TokenPriceDto tokenPriceDto = priceFindService.findTokenPrice(tokenPriceHelper.getDexTokenId(token), lastTokenPriceTime);


            if (!lastTokenPriceTime.isEqual(tokenPriceDto.getLastPriceTime())) {
                for (TokenPriceDto.PriceInTime priceInTime : tokenPriceDto.getPriceInTimeList()) {
                    E tokenPrice = tokenPriceHelper.tokenPriceFromPriceInTime(priceInTime, token);
                    preparedTokenPriceList.add(tokenPrice);
                }
            }
        } else {
            TokenPriceDto tokenPriceDto;
            if (isExistsPricesForCurrentToken) {
                LocalDateTime firstTokenPriceTimeWithDexIsNull = existsTokenPricesWithPriceIsNull.get(0).getTokenPriceId().getDatetime();
                tokenPriceDto = priceFindService.findTokenPrice(tokenPriceHelper.getDexTokenId(token), firstTokenPriceTimeWithDexIsNull);
            } else {
                tokenPriceDto = priceFindService.findTokenPrice(tokenPriceHelper.getDexTokenId(token), TimeHelper.millisToLocalDateTime(DateConstants.START_DATE_IN_MILLIS));
            }

            for (TokenPriceDto.PriceInTime priceInTime : tokenPriceDto.getPriceInTimeList()) {
                LocalDateTime currentConcreteTime = priceInTime.getTime();
                for (E tokenPrice : existsTokenPricesWithPriceIsNull) {
                    if (currentConcreteTime.isBefore(tokenPrice.getTokenPriceId().getDatetime()) && !isExistsPricesForCurrentToken) {
                        preparedTokenPriceList.add(tokenPriceHelper.tokenPriceFromPriceInTime(priceInTime, token));
                        break;
                    } else if (currentConcreteTime.isEqual(tokenPrice.getTokenPriceId().getDatetime())) {
                        tokenPrice.setPriceInUsd(priceInTime.getPrice());
                        preparedTokenPriceList.add(tokenPrice);
                        break;
                    }
                }
            }
        }

        log.info("Ready to save all preparedTokenPriceList in CoingeckoSymbolPriceInUsd, size: " + preparedTokenPriceList.size());
        repository.saveAll(preparedTokenPriceList);
        log.info("Saved all preparedTokenPriceList in CoingeckoSymbolPriceInUsd, size: " + preparedTokenPriceList.size());
        return preparedTokenPriceList.size();
    }
}
