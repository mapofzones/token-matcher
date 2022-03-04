package com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices;

import com.mapofzones.tokenmatcher.service.tokenprice.client.ITokenPriceClient;
import com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices.dto.TokenPriceDto;
import com.mapofzones.tokenmatcher.utils.TimeHelper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractPriceFindService {

    private final ITokenPriceClient client;

    public AbstractPriceFindService(ITokenPriceClient client) {
        this.client = client;
    }

    public TokenPriceDto findTokenPrice(String tokenId, LocalDateTime lastTokenPriceTime) {

        TokenPriceDto foundTokenPrices = client.findTokenPrice(tokenId, lastTokenPriceTime).toTokenPrice();

        LocalDateTime firstTokenTime = foundTokenPrices.getFirstPriceTime();
        if (firstTokenTime.isBefore(TimeHelper.nowAroundHours())) {
            completeTokenPrices(foundTokenPrices);
        }
        return foundTokenPrices;
    }

    private void completeTokenPrices(TokenPriceDto foundTokenPrices) {

        List<TokenPriceDto.PriceInTime> additionalTokenPriceDtoSet = new ArrayList<>();
        LocalDateTime expectedPriceTime = foundTokenPrices.getFirstPriceTime();
        LocalDateTime lastPriceTime = foundTokenPrices.getLastPriceTime();

        for (int i = 0; i < foundTokenPrices.getPriceInTimeList().size(); i++) {
            while ((!(expectedPriceTime.isEqual(foundTokenPrices.getPriceInTimeList().get(i).getTime())) && expectedPriceTime.isBefore(lastPriceTime)) ||
                    ((expectedPriceTime.isAfter(lastPriceTime) || expectedPriceTime.isEqual(lastPriceTime)) && (expectedPriceTime.isBefore(TimeHelper.nowAroundHours())))) {

                if (foundTokenPrices.getPriceInTimeList().get(i).getTime().isEqual(foundTokenPrices.getPriceInTimeList().get(i - 1).getTime())) {
                    expectedPriceTime = expectedPriceTime.minus(1, ChronoUnit.HOURS);
                } else if (foundTokenPrices.getPriceInTimeList().get(i).getTime().isEqual(expectedPriceTime)) {
                    expectedPriceTime = expectedPriceTime.plus(1, ChronoUnit.HOURS);
                } else {
                    TokenPriceDto.PriceInTime priceInTime =
                            new TokenPriceDto.PriceInTime(expectedPriceTime, foundTokenPrices.getPriceInTimeList().get(expectedPriceTime.isBefore(lastPriceTime) ? i - 1 : i).getPrice());
                    additionalTokenPriceDtoSet.add(priceInTime);
                    expectedPriceTime = expectedPriceTime.plus(1, ChronoUnit.HOURS);
                }
            }
            expectedPriceTime = expectedPriceTime.plus(1, ChronoUnit.HOURS);
            if (!additionalTokenPriceDtoSet.isEmpty() && additionalTokenPriceDtoSet.size() < 48) {
                additionalTokenPriceDtoSet.forEach(value -> foundTokenPrices.getPriceInTimeList().add(value));
                additionalTokenPriceDtoSet.clear();
            }
        }
        foundTokenPrices.getPriceInTimeList().sort(Comparator.comparing(TokenPriceDto.PriceInTime::getTime));
    }
}
