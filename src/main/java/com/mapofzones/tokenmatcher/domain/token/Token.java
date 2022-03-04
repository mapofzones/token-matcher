package com.mapofzones.tokenmatcher.domain.token;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "TOKENS")
public class Token {

    @EmbeddedId
    private TokenId tokenId;

    @Column(name = "coingecko_id")
    private String coingeckoId;

    @Column(name = "osmosis_id")
    private String osmosisId;
}
