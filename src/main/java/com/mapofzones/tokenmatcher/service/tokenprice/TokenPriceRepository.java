package com.mapofzones.tokenmatcher.service.tokenprice;

import com.mapofzones.tokenmatcher.domain.TokenPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenPriceRepository extends JpaRepository<TokenPrice, TokenPrice.TokenPriceId> {

    @Query(value = "SELECT tp.datetime FROM token_prices tp WHERE tp.base_denom = ?1 ORDER BY tp.datetime DESC LIMIT 1", nativeQuery = true)
    Optional<LocalDateTime> findLastTimeOfTokenPriceByZone(String baseDenom);

    @Query(value = "SELECT * FROM token_prices tp WHERE tp.base_denom = ?1 AND tp.coingecko_symbol_price_in_usd is null AND tp.datetime > (SELECT etp.datetime FROM token_prices etp WHERE etp.base_denom = ?1 AND etp.coingecko_symbol_price_in_usd is not null ORDER BY etp.datetime ASC LIMIT 1) ORDER BY tp.datetime ASC", nativeQuery = true)
    List<TokenPrice> findAllAfterFirstByZoneAndCoingeckoPriceIsNull(String baseDenom);

    @Query(value = "SELECT * FROM token_prices tp WHERE tp.base_denom = ?1 AND tp.coingecko_symbol_price_in_usd is null ORDER BY tp.datetime ASC", nativeQuery = true)
    List<TokenPrice> findAllByZoneAndCoingeckoPriceIsNull(String baseDenom);

    @Query(value = "SELECT * FROM token_prices tp WHERE tp.base_denom = ?1 AND tp.osmosis_symbol_price_in_usd is null AND tp.datetime > (SELECT etp.datetime FROM token_prices etp WHERE etp.base_denom = ?1 AND etp.osmosis_symbol_price_in_usd is not null ORDER BY etp.datetime ASC LIMIT 1) ORDER BY tp.datetime ASC", nativeQuery = true)
    List<TokenPrice> findAllAfterFirstByZoneAndOsmosisPriceIsNull(String baseDenom);

    @Query(value = "SELECT * FROM token_prices tp WHERE tp.base_denom = ?1 AND tp.osmosis_symbol_price_in_usd is null ORDER BY tp.datetime ASC", nativeQuery = true)
    List<TokenPrice> findAllByZoneAndOsmosisPriceIsNull(String baseDenom);

    Boolean existsByCoingeckoSymbolPriceInUsdIsNotNullAndTokenPriceId_BaseDenom(String baseDenom);
    Boolean existsByOsmosisSymbolPriceInUsdIsNotNullAndTokenPriceId_BaseDenom(String baseDenom);
}
