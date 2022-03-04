package com.mapofzones.tokenmatcher.domain.tokenprice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TokenPriceId implements Serializable {

    @Column(name = "ZONE")
    private String zone;

    @Column(name = "BASE_DENOM")
    private String baseDenom;

    @Column(name = "DATETIME")
    private LocalDateTime datetime;
}