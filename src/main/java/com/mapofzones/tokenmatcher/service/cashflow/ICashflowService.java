package com.mapofzones.tokenmatcher.service.cashflow;

import java.util.Set;

import com.mapofzones.tokenmatcher.domain.Cashflow;

public interface ICashflowService {

	Set<Cashflow> findUnmatchedCashflow();

}
