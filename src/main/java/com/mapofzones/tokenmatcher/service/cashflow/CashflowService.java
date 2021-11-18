package com.mapofzones.tokenmatcher.service.cashflow;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mapofzones.tokenmatcher.domain.Cashflow;

@Service
@Transactional(readOnly = true)
public class CashflowService implements ICashflowService {
	
	private final CashflowRepository cashflowRepository;
	
	public CashflowService(CashflowRepository cashflowRepository) {
		this.cashflowRepository = cashflowRepository;
	}

	@Override
	public Set<Cashflow> findUnmatchedCashflow() {
		return cashflowRepository.findUnmatchedCashflow();
	}
	
	

}
