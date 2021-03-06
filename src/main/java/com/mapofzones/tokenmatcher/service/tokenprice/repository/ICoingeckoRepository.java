package com.mapofzones.tokenmatcher.service.tokenprice.repository;

import com.mapofzones.tokenmatcher.domain.tokenprice.CoingeckoTokenPrice;
import com.mapofzones.tokenmatcher.domain.tokenprice.TokenPriceId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICoingeckoRepository extends TokenPriceRepository<CoingeckoTokenPrice, TokenPriceId> {

    @Query(value =
            "SELECT * FROM token_prices tp " +
            "   WHERE tp.base_denom = ?1 " +
            "       AND tp.zone = ?2 " +
            "       AND (tp.coingecko_symbol_price_in_usd IS NULL " +
            "       OR tp.coingecko_symbol_market_cap_in_usd IS NULL " +
            "       OR tp.coingecko_symbol_total_volumes_in_usd IS NULL) " +
            "       AND tp.datetime > (" +
            "           SELECT etp.datetime FROM token_prices etp " +
            "               WHERE etp.base_denom = ?1 " +
            "                   AND etp.zone = ?2 " +
            "                   AND (etp.coingecko_symbol_price_in_usd IS NOT NULL " +
            "                   OR etp.coingecko_symbol_market_cap_in_usd IS NOT NULL " +
            "                   OR etp.coingecko_symbol_total_volumes_in_usd IS NOT NULL) " +
            "               ORDER BY etp.datetime LIMIT 1) " +
            "       ORDER BY tp.datetime", nativeQuery = true)
    List<CoingeckoTokenPrice> findAllAfterFirstByBaseDenomAndZoneAndDexPriceIsNull(String baseDenom, String zone);

    @Query(value =
            "SELECT * FROM token_prices tp " +
            "   WHERE tp.base_denom = ?1 " +
            "      AND tp.zone = ?2 " +
            "      AND (tp.coingecko_symbol_price_in_usd IS NULL " +
            "      OR tp.coingecko_symbol_total_volumes_in_usd IS NULL " +
            "      OR tp.coingecko_symbol_market_cap_in_usd IS NULL) " +
            "      ORDER BY tp.datetime", nativeQuery = true)
    List<CoingeckoTokenPrice> findAllByBaseDenomAndZoneAndDexPriceIsNull(String baseDenom, String zone);

    @Query(value =
            "select case when exists(" +
            "   SELECT * FROM token_prices tp " +
            "      WHERE tp.coingecko_symbol_price_in_usd IS NOT NULL " +
            "          AND tp.coingecko_symbol_market_cap_in_usd IS NOT NULL " +
            "          AND tp.coingecko_symbol_total_volumes_in_usd IS NOT NULL " +
            "          AND tp.base_denom = ?1 " +
            "          AND tp.zone = ?2) " +
            "   then true else false end", nativeQuery = true)
    Boolean existsByDexSymbolPriceInUsdIsNotNullAndTokenPriceId_BaseDenom(String baseDenom, String zone);

}
