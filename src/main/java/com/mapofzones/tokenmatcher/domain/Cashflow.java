package com.mapofzones.tokenmatcher.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "IBC_TRANSFER_HOURLY_CASHFLOW")
public class Cashflow {

	@Embeddable
	public static class CashflowId implements Serializable{
		@Column(name = "ZONE")
		String zone;
		@Column(name = "DENOM")
		String denom;
	}
	
	@EmbeddedId
	private CashflowId keyId;
	
	
}
