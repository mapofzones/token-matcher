package com.mapofzones.tokenmatcher.service.derivative;

import com.mapofzones.tokenmatcher.domain.Cashflow;
import com.mapofzones.tokenmatcher.domain.Derivative;

import java.util.List;

public interface IDerivativeService {

	Derivative findById(Derivative.DerivativeId id);
	Derivative save(Derivative derivative);
	void saveBatch(List<Derivative> derivativeList);
	Boolean isExists(Derivative.DerivativeId derivativeId);
	Derivative buildViaCashFlow(Cashflow cashflow);

}
