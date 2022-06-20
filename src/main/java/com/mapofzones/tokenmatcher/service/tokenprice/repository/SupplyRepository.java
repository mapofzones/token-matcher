package com.mapofzones.tokenmatcher.service.tokenprice.repository;

import com.mapofzones.tokenmatcher.domain.tokenprice.TokenPriceId;
import com.mapofzones.tokenmatcher.domain.tokenprice.TokenSupply;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplyRepository extends TokenPriceRepository<TokenSupply, TokenPriceId> {

    @Query(value =
            "SELECT * FROM token_prices tp " +
            "   WHERE tp.base_denom = ?1 " +
            "       AND tp.zone = ?2 " +
            "       AND tp.symbol_supply is null " +
            "       AND tp.datetime > " +
            "           (SELECT etp.datetime FROM token_prices etp " +
            "               WHERE etp.base_denom = ?1 " +
            "                   AND etp.zone = ?2 " +
            "                   AND etp.symbol_supply is not null " +
            "               ORDER BY etp.datetime LIMIT 1) " +
            "   ORDER BY tp.datetime", nativeQuery = true)
    List<TokenSupply> findAllAfterFirstByBaseDenomAndZoneAndDexPriceIsNull(String baseDenom, String zone);

    @Query(value =
            "SELECT * FROM token_prices tp " +
            "   WHERE tp.base_denom = ?1 " +
            "       AND tp.symbol_supply is null " +
            "   ORDER BY tp.datetime", nativeQuery = true)
    List<TokenSupply> findAllByBaseDenomAndZoneAndDexPriceIsNull(String baseDenom, String zone);

    @Query(value = "" +
            "SELECT CASE WHEN exists(" +
            "   Select *  FROM token_prices tp " +
            "       WHERE tp.symbol_supply IS NOT NULL " +
            "       AND tp.base_denom = ?1 AND tp.zone = ?2) THEN TRUE ELSE FALSE END", nativeQuery = true)
    Boolean existsByDexSymbolPriceInUsdIsNotNullAndTokenPriceId_BaseDenom(String baseDenom, String zone);
}
