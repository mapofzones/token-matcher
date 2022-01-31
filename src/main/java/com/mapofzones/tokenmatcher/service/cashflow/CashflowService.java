package com.mapofzones.tokenmatcher.service.cashflow;

import com.mapofzones.tokenmatcher.common.exceptions.EntityNotFoundException;
import com.mapofzones.tokenmatcher.domain.Cashflow;
import com.mapofzones.tokenmatcher.domain.Derivative;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CashflowService implements ICashflowService {
	
	private final CashflowRepository cashflowRepository;

	public CashflowService(CashflowRepository cashflowRepository) {
		this.cashflowRepository = cashflowRepository;
	}

	@Override
	public Cashflow findById(Cashflow.CashflowId id) {
		return cashflowRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString()));
	}

	@Override
	public List<Cashflow> findUnmatchedCashflow() {
		return cashflowRepository.findAllByDerivativeDenomIsNullAndDenomIsNotEmpty();
	}

	@Override
	public Cashflow matchWithDerivative(Cashflow.CashflowId cashflowId, Derivative.DerivativeId derivativeId) {
		Cashflow cashflow = findById(cashflowId);
		cashflow.setDerivativeDenom(derivativeId.getFullDenom());
		return cashflow;
	}
}