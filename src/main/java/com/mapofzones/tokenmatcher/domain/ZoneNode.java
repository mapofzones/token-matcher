package com.mapofzones.tokenmatcher.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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

	@Column(name = "LCD_ADDR")
	private String lcdAddress;

	@Column(name = "IS_LCD_ADDR_ACTIVE")
	private Boolean isLcdAddressActive;
	
}
