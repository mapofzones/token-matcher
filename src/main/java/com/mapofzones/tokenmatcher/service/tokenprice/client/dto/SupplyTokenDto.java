package com.mapofzones.tokenmatcher.service.tokenprice.client.dto;

import com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices.dto.TokenPriceDto;
import com.mapofzones.tokenmatcher.utils.TimeHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SupplyTokenDto extends AbstractDexTokenPriceDto {

    private String supply;

    public SupplyTokenDto(String supply) {
        this.supply = supply;
    }

    @Override
    public TokenPriceDto toTokenPrice() {
        List<TokenPriceDto.PriceInTime> priceInTimeList = new ArrayList<>();
        priceInTimeList.add(
                new TokenPriceDto.PriceInTime(TimeHelper.nowAroundHours(), supply != null ? new BigDecimal(supply) : null)
        );
        return new TokenPriceDto(priceInTimeList);
    }
}
