package com.mapofzones.tokenmatcher.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "IBC_TRANSFER_HOURLY_CASHFLOW")
public class Cashflow {

	@Data
	@Embeddable
	public static class CashflowId implements Serializable{
		@Column(name = "ZONE")
		private String zone;

		@Column(name = "ZONE_SRC")
		private String zoneSource;

		@Column(name = "ZONE_DEST")
		private String zoneDestination;

		@Column(name = "HOUR")
		private LocalDateTime hour;

		@Column(name = "PERIOD")
		private Integer period;

		@Column(name = "IBC_CHANNEL")
		private String ibcChannel;

		@Column(name = "DENOM")
		private String denom;
	}
	
	@EmbeddedId
	private CashflowId cashflowId;

	@Column(name = "derivative_denom")
	private String derivativeDenom;
}
