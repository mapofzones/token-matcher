package com.mapofzones.tokenmatcher.domain;

import com.mapofzones.tokenmatcher.service.derivative.client.DenomTraceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
@Data
@Table(name = "DERIVATIVES")
public class Derivative {

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Embeddable
	public static class DerivativeId implements Serializable {

		@Column(name = "ZONE")
		private String zone;

		@Column(name = "FULL_DENOM")
		private String fullDenom;
	}

	@EmbeddedId
	private DerivativeId derivativeId;

	@Column(name = "BASE_DENOM")
	private String baseDenom;

	@Column(name = "ORIGIN_ZONE")
	private String originZone;

	@Transient
	private boolean isSuccessDenomTraceReceived = true;

	public void merge(DenomTraceDto dto) {
		derivativeId.fullDenom = dto.getFullDenom() + "/" + dto.getBaseDenom();
		this.baseDenom = dto.getBaseDenom();
		this.isSuccessDenomTraceReceived = dto.isSuccessReceived();
	}
}
