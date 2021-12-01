package com.mapofzones.tokenmatcher.service.cashflow;

import java.util.List;
import java.util.Set;

import com.mapofzones.tokenmatcher.domain.Cashflow;
import com.mapofzones.tokenmatcher.domain.Derivative;

public interface ICashflowService {

	Cashflow findById(Cashflow.CashflowId id);
	List<Cashflow> findUnmatchedCashflow();
	Cashflow matchWithDerivative(Cashflow.CashflowId cashflowId, Derivative.DerivativeId derivativeId);

}
