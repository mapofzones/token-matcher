package com.mapofzones.tokenmatcher.service;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.mapofzones.tokenmatcher.domain.Derivative;
import com.mapofzones.tokenmatcher.service.derivative.IDerivativeService;

@Component
public class TokenMatcherFacade {

	private final IDerivativeService derivativeService;
	
	public TokenMatcherFacade(IDerivativeService derivativeService) {
		this.derivativeService = derivativeService;
	}

	public void match() {
		Set<Derivative> unmatchedDerivatives = derivativeService.findUnmatchedDerivatives();
		unmatchedDerivatives.forEach(ud -> derivativeService.match(ud.getDerivativeId()));
	}
}
