package com.mapofzones.tokenmatcher.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "ZONE_NODES")
public class ZoneNode {

	@Id
	@Column(name = "RPC_ADDR")
	private String rpcAddress;
	
	@Column(name = "ZONE")
	private String zone;
	
	@Column(name = "IS_ALIVE")
	private Boolean isAlive;
	
}
