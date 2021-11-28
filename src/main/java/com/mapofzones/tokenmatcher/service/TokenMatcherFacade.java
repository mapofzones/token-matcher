package com.mapofzones.tokenmatcher.service;

import com.mapofzones.tokenmatcher.common.properties.TokenMatcherProperties;
import com.mapofzones.tokenmatcher.domain.Cashflow;
import com.mapofzones.tokenmatcher.domain.Derivative;
import com.mapofzones.tokenmatcher.service.cashflow.ICashflowService;
import com.mapofzones.tokenmatcher.service.derivative.IDerivativeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class TokenMatcherFacade {

	private final TokenMatcherProperties properties;
	private final IDerivativeService derivativeService;
	private final ICashflowService cashflowService;

	private BlockingQueue<Cashflow> cashflowQueue;

	public TokenMatcherFacade(IDerivativeService derivativeService,
							  ICashflowService cashflowService,
							  TokenMatcherProperties properties) {
		this.properties = properties;
		this.derivativeService = derivativeService;
		this.cashflowService = cashflowService;
	}

	public void matchAll() {
		List<Cashflow> unmatchedCashflowList = cashflowService.findUnmatchedCashflow();
		cashflowQueue = new ArrayBlockingQueue<>(unmatchedCashflowList.size(), true, unmatchedCashflowList);
		runMatcherThreads();
		List<CompletableFuture<Void>> futures = new ArrayList<>(runMatcherThreads());
		waitThreads(futures);
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

	private List<CompletableFuture<Void>> runMatcherThreads() {
		List<CompletableFuture<Void>> cfList = new ArrayList<>();
		for (int i = 0; i < properties.getThreads(); i++) {

			int finalI = i;
			cfList.add(CompletableFuture
					.runAsync(() -> {
						while (true) {
							if (!cashflowQueue.isEmpty()) {
								try {
									Cashflow currentCashflow = cashflowQueue.take();
									match(currentCashflow);
									log.info("Thread-" + finalI + " Start matching " + currentCashflow);
								} catch (InterruptedException e) {
									log.error("Thread error. " + e.getCause());
									e.printStackTrace();
								}
							}
							else break;
						}
					})
					.thenRun(() -> log.info("Thread-" + finalI + " is finished")));
		}
		return cfList;
	}

	private void waitThreads(List<CompletableFuture<Void>> futures) {
		futures.forEach(cf -> {
			try {
				cf.get();
			} catch (InterruptedException | ExecutionException e) {
				log.error("Thread error. " + e.getCause());
				e.printStackTrace();
			}
		});
	}
}
