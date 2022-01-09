package com.mapofzones.tokenmatcher.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "TOKENS")
public class Token {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class TokenId implements Serializable {

        @Column(name = "ZONE")
        private String zone;

        @Column(name = "BASE_DENOM")
        private String baseDenom;
    }

    @EmbeddedId
    private Token.TokenId tokenId;

    @Column(name = "coingecko_id")
    private String coingeckoId;

    @Column(name = "osmosis_id")
    private String osmosisId;

}
