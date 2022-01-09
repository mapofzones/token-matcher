package com.mapofzones.tokenmatcher.service.token;

import com.mapofzones.tokenmatcher.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Token.TokenId> {

    List<Token> findAllByCoingeckoIdIsNotNull();

    List<Token> findAllByOsmosisIdIsNotNull();

    @Modifying
    @Query(value = "INSERT INTO tokens (zone, base_denom) VALUES (?1, ?2) ON CONFLICT DO NOTHING", nativeQuery = true)
    void save(String zone, String baseDenom);

}
