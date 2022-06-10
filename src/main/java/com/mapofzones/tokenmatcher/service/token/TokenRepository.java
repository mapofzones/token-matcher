package com.mapofzones.tokenmatcher.service.token;

import com.mapofzones.tokenmatcher.domain.token.Token;
import com.mapofzones.tokenmatcher.domain.token.TokenId;
import com.mapofzones.tokenmatcher.service.base.IGenericRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends IGenericRepository<Token, TokenId> {
    List<Token> findAllByCoingeckoIdIsNotNullOrderByPriceLastCheckedAtAsc();

    List<Token> findAllByOsmosisIdIsNotNull();

    @Modifying
    @Query(value = "INSERT INTO tokens (zone, base_denom) VALUES (?1, ?2) ON CONFLICT DO NOTHING", nativeQuery = true)
    void save(String zone, String baseDenom);

}
