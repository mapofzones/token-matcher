package com.mapofzones.tokenmatcher.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	private boolean isSuccessfulBuild = true;

	public void setDenomTraceData(DenomTrace denomTrace) {
		derivativeId.fullDenom = denomTrace.getPath() + "/" + denomTrace.getBaseDenom();
		this.isSuccessfulBuild = denomTrace.isSuccessfulReceived();
	}

	public void setTokenIdData(Token.TokenId tokenId) {
		this.setBaseDenom(tokenId.getBaseDenom());
		this.setOriginZone(tokenId.getZone());
	}
}
