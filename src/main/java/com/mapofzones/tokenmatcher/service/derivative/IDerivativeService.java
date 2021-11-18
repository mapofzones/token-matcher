package com.mapofzones.tokenmatcher.service.derivative;

import java.util.Set;

import com.mapofzones.tokenmatcher.domain.Derivative;

public interface IDerivativeService {
	
	public Set<Derivative> findUnmatchedDerivatives();
	public Derivative match(Derivative.DerivativeId id);

}
