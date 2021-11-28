package com.mapofzones.tokenmatcher.service.cashflow;

import java.util.List;

import com.mapofzones.tokenmatcher.common.exceptions.EntityNotFoundException;
import com.mapofzones.tokenmatcher.domain.Derivative;
import com.mapofzones.tokenmatcher.service.derivative.IDerivativeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mapofzones.tokenmatcher.domain.Cashflow;

@Service
@Transactional(propagation = Propagation.NESTED)
public class CashflowService implements ICashflowService {
	
	private final CashflowRepository cashflowRepository;

	public CashflowService(CashflowRepository cashflowRepository) {
		this.cashflowRepository = cashflowRepository;
	}

	@Override
	public void saveBatch(List<Cashflow> cashflowList) {
		cashflowRepository.saveAll(cashflowList);
	}

	@Override
	public Cashflow findById(Cashflow.CashflowId id) {
		return cashflowRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString()));
	}

	@Override
	public List<Cashflow> findUnmatchedCashflow() {
		return cashflowRepository.findAllByDerivativeDenomIsNull();
	}

	@Override
	public Cashflow matchWithDerivative(Cashflow.CashflowId cashflowId, Derivative.DerivativeId derivativeId) {
		Cashflow cashflow = findById(cashflowId);
		cashflow.setDerivativeDenom(derivativeId.getFullDenom());
		return cashflow;
	}
}