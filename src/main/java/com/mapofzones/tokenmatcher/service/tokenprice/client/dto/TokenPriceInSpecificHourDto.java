package com.mapofzones.tokenmatcher.service.tokenprice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenPriceInSpecificHourDto {

    private Long timeInMillis;
    private BigDecimal price;

}
