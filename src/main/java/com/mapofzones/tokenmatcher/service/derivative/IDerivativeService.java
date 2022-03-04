package com.mapofzones.tokenmatcher.service.derivative;

import com.mapofzones.tokenmatcher.domain.Cashflow;
import com.mapofzones.tokenmatcher.domain.Derivative;
import com.mapofzones.tokenmatcher.domain.token.TokenId;

import java.util.List;

public interface IDerivativeService {

	void save(Derivative derivative);
	List<Derivative> findIncomplete();
	Derivative buildViaCashFlow(Cashflow cashflow);
	void setTokenIdData(Derivative.DerivativeId derivativeId, TokenId tokenId);

}
