package com.mapofzones.tokenmatcher.service;

import com.mapofzones.tokenmatcher.common.properties.TokenMatcherProperties;
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

	private final TokenMatcherProperties properties;
	private final IDerivativeService derivativeService;
	private final ICashflowService cashflowService;
	private final IThreadStarter threadStarter;

	private BlockingQueue<Cashflow> cashflowQueue;

	public TokenMatcherFacade(IDerivativeService derivativeService,
							  ICashflowService cashflowService,
							  TokenMatcherProperties properties,
							  IThreadStarter threadStarter) {
		this.properties = properties;
		this.derivativeService = derivativeService;
		this.cashflowService = cashflowService;
		this.threadStarter = threadStarter;
	}

	public void matchAll() {
		List<Cashflow> unmatchedCashflowList = cashflowService.findUnmatchedCashflow();
		cashflowQueue = new ArrayBlockingQueue<>(unmatchedCashflowList.size(), true, unmatchedCashflowList);
		threadStarter.startThreads(tokenMatcherFunction, properties.getThreads(), properties.getThreadsNaming());
	}

	@Transactional
	public void match(Cashflow cashflow) {
		Derivative builtDerivative = derivativeService.buildViaCashFlow(cashflow);

		if (builtDerivative.isSuccessDenomTraceReceived()) {
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
					log.info(Thread.currentThread().getName() + " Start matching " + currentCashflow);
				} catch (InterruptedException e) {
					log.error("Queue error. " + e.getCause());
					e.printStackTrace();
				}
			}
			else break;
		}
	};
}
