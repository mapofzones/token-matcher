package com.mapofzones.tokenmatcher.service.tokenprice;

import com.mapofzones.tokenmatcher.domain.TokenPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TokenPriceRepository extends JpaRepository<TokenPrice, TokenPrice.TokenPriceId> {

    @Query(value = "SELECT tp.datetime FROM token_prices tp WHERE tp.zone = ?1 ORDER BY tp.datetime DESC LIMIT 1", nativeQuery = true)
    LocalDateTime findLastTokenPriceByZone(String zone);

}
