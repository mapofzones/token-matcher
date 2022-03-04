package com.mapofzones.tokenmatcher.domain.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TokenId implements Serializable {

    @Column(name = "ZONE")
    private String zone;

    @Column(name = "BASE_DENOM")
    private String baseDenom;
}
