package com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices;

import com.mapofzones.tokenmatcher.service.tokenprice.client.OsmosisClient;
import com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices.dto.TokenPriceDto;
import com.mapofzones.tokenmatcher.utils.TimeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class AbstractPriceFindServiceTest {

    @Mock
    OsmosisClient osmosisClient;

    @Test
    void completeTokenPricesTest_MissEntriesInMiddleOfList() {

        String privateMethodName = "completeTokenPrices";

        int testEntries = 10;
        int missEntries = 3;
        long startPrice = 100;
        int startPlaceOfMissEntries = 5;
        LocalDateTime startTime = TimeHelper.nowAroundHours().minusDays(2);

        AbstractPriceFindService osmosisPriceFindService = new OsmosisPriceFindService(osmosisClient);
        List<TokenPriceDto.PriceInTime> priceInTimeList = new ArrayList<>();

        for (int i = 0; i <= testEntries; i++)
            priceInTimeList.add(new TokenPriceDto.PriceInTime(startTime.plus((i > startPlaceOfMissEntries) ?  i + missEntries : i, ChronoUnit.HOURS), BigDecimal.valueOf(startPrice + i)));

        TokenPriceDto tokenPriceDto = new TokenPriceDto(priceInTimeList);

        ReflectionTestUtils.invokeMethod(osmosisPriceFindService, privateMethodName, tokenPriceDto);

        long currentPrice = startPrice;
        for (int i = 0; i <= testEntries + missEntries; i++) {
            currentPrice = (i <= startPlaceOfMissEntries) ? i + startPrice : currentPrice;

            if (i > startPlaceOfMissEntries + missEntries)
                currentPrice++;

            TokenPriceDto.PriceInTime expectedPrice = new TokenPriceDto.PriceInTime(startTime.plus(i, ChronoUnit.HOURS), BigDecimal.valueOf(currentPrice));
            TokenPriceDto.PriceInTime actualPrice = tokenPriceDto.getPriceInTimeList().get(i);
            Assertions.assertEquals(expectedPrice, actualPrice);
        }
    }

    @Test
    void completeTokenPricesTest_OnePriceInDay() {

        String privateMethodName = "completeTokenPrices";

        int testEntries = 2;
        long startPrice = 100;
        LocalDateTime startTime = TimeHelper.nowAroundDays();

        AbstractPriceFindService osmosisPriceFindService = new OsmosisPriceFindService(osmosisClient);
        List<TokenPriceDto.PriceInTime> priceInTimeList = new ArrayList<>();

        for (int i = 0; i <= testEntries; i++)
            priceInTimeList.add(new TokenPriceDto.PriceInTime(startTime.minus(testEntries - i, ChronoUnit.DAYS), BigDecimal.valueOf(startPrice + i)));

        TokenPriceDto tokenPriceDto = new TokenPriceDto(priceInTimeList);

        ReflectionTestUtils.invokeMethod(osmosisPriceFindService, privateMethodName, tokenPriceDto);

        for (int i = 0; i < tokenPriceDto.getPriceInTimeList().size(); i++) {
                TokenPriceDto.PriceInTime expectedPrice =
                        new TokenPriceDto.PriceInTime(startTime.minus(testEntries, ChronoUnit.DAYS).plus(i, ChronoUnit.HOURS), BigDecimal.valueOf((i / 24) + startPrice));
                TokenPriceDto.PriceInTime actualPrice = tokenPriceDto.getPriceInTimeList().get(i);
                Assertions.assertEquals(expectedPrice, actualPrice);
        }
    }
}
