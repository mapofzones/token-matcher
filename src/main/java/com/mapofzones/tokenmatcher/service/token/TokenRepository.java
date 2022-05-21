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
    @Query(value = "" +
            "SELECT\n" +
            "    zone,\n" +
            "    base_denom,\n" +
            "    coingecko_id,\n" +
            "    osmosis_id\n" +
            "from (\n" +
            "    SELECT \n" +
            "        tokens.zone,\n" +
            "        tokens.base_denom,\n" +
            "        tokens.coingecko_id,\n" +
            "        tokens.osmosis_id,\n" +
            "        max(datetime) as datetime\n" +
            "    FROM\n" +
            "        public.tokens\n" +
            "    LEFT JOIN public.token_prices\n" +
            "        on tokens.zone = token_prices.zone and tokens.base_denom = token_prices.base_denom \n" +
            "    WHERE\n" +
            "        coingecko_id is not NULL\n" +
            "        -- and token_prices.datetime is not NULL\n" +
            "    GROUP BY \n" +
            "        tokens.zone,\n" +
            "        tokens.base_denom\n" +
            ") as data\n" +
            "ORDER BY\n" +
            "    datetime ASC", nativeQuery = true)
    List<Token> findAllByCoingeckoIdIsNotNullOrderByDatetimeDesc();

    List<Token> findAllByOsmosisIdIsNotNull();

    @Modifying
    @Query(value = "INSERT INTO tokens (zone, base_denom) VALUES (?1, ?2) ON CONFLICT DO NOTHING", nativeQuery = true)
    void save(String zone, String baseDenom);

}
