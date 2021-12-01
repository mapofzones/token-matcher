package com.mapofzones.tokenmatcher.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "ZONES")
public class Zone {

    @Id
    @Column(name = "CHAIN_ID")
    private String chainId;

}
