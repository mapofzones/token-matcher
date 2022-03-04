package com.mapofzones.tokenmatcher.service.tokenprice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface TokenPriceRepository<T, ID> extends JpaRepository<T, ID> {

    @Query(value = "SELECT tp.datetime FROM token_prices tp WHERE tp.base_denom = ?1 AND tp.zone = ?2 ORDER BY tp.datetime DESC LIMIT 1", nativeQuery = true)
    Optional<LocalDateTime> findLastTimeOfTokenPriceByBaseDenomAndZone(String baseDenom, String zone);

    List<T> findAllAfterFirstByBaseDenomAndZoneAndDexPriceIsNull(String baseDenom, String zone);
    List<T> findAllByBaseDenomAndZoneAndDexPriceIsNull(String baseDenom, String zone);
    Boolean existsByDexSymbolPriceInUsdIsNotNullAndTokenPriceId_BaseDenom(String baseDenom, String zone);

}
