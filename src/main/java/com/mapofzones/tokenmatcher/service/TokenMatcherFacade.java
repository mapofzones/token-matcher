package com.mapofzones.tokenmatcher.service;

import com.mapofzones.tokenmatcher.common.threads.IThreadStarter;
import com.mapofzones.tokenmatcher.domain.Cashflow;
import com.mapofzones.tokenmatcher.domain.Derivative;
import com.mapofzones.tokenmatcher.service.cashflow.ICashflowService;
import com.mapofzones.tokenmatcher.service.derivative.IDerivativeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Service
public class TokenMatcherFacade {

	private final IDerivativeService derivativeService;
	private final ICashflowService cashflowService;
	private final IThreadStarter tokenMatcherThreadStarter;

	private BlockingQueue<Cashflow> cashflowQueue;

	public TokenMatcherFacade(IDerivativeService derivativeService,
							  ICashflowService cashflowService,
							  IThreadStarter tokenMatcherThreadStarter) {
		this.derivativeService = derivativeService;
		this.cashflowService = cashflowService;
		this.tokenMatcherThreadStarter = tokenMatcherThreadStarter;
	}

	public void matchAll() {
		List<Cashflow> unmatchedCashflowList = cashflowService.findUnmatchedCashflow();
		if (!unmatchedCashflowList.isEmpty()) {
			cashflowQueue = new ArrayBlockingQueue<>(unmatchedCashflowList.size(), true, unmatchedCashflowList);
			tokenMatcherThreadStarter.startThreads(tokenMatcherFunction);
		}
	}

	@Transactional
	public void match(Cashflow cashflow) {
		Derivative builtDerivative = derivativeService.buildViaCashFlow(cashflow);

		if (builtDerivative.isSuccessfulBuild()) {
			Derivative savedDerivative = derivativeService.save(builtDerivative);
			log.info("Saved: " + savedDerivative.toString());
			Cashflow matchedCashflow = cashflowService.matchWithDerivative(cashflow.getCashflowId(), savedDerivative.getDerivativeId());
			log.info("Matched with derivative denom: " + matchedCashflow.getDerivativeDenom());
		}
	}

	private final Runnable tokenMatcherFunction = () -> {
		while (true) {
			if (!cashflowQueue.isEmpty()) {
				try {
					Cashflow currentCashflow = cashflowQueue.take();
					match(currentCashflow);
					//log.info(Thread.currentThread().getName() + " Start matching " + currentCashflow);
				} catch (InterruptedException e) {
					log.error("Queue error. " + e.getCause());
					e.printStackTrace();
				}
			}
			else break;
		}
	};
}
