package com.mapofzones.tokenmatcher.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mapofzones.tokenmatcher.service.derivative.client.DenomTraceDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "DERIVATIVES")
public class Derivative {
	
	@Embeddable
	@ToString
	@Getter
	public static class DerivativeId implements Serializable{
		@Column(name = "ZONE")
		String zone;
		@Column(name = "DENOM")
		String denom;
	}
	
	@Id
	private DerivativeId derivativeId; 
	@Column(name = "PATH")
	private String path;
	@Column(name = "BASE_DENOM")
	private String baseDenom;
	@Column(name = "ORIGIN_ZONE")
	private String originZone;
	
	
	public void merge(DenomTraceDto dto) {
		this.path = dto.getPath();
		this.baseDenom = dto.getBaseDenom();
	}
	
}
