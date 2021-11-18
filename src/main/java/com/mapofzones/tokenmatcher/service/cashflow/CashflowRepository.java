package com.mapofzones.tokenmatcher.service.cashflow;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mapofzones.tokenmatcher.domain.Cashflow;

@Repository
public interface CashflowRepository extends JpaRepository<Cashflow, Cashflow.CashflowId>{

	@Query(nativeQuery = true, 
			value = "select distinct\n"
			+ "    cashflow.zone,\n"
			+ "    cashflow.denom\n"
			+ "from\n"
			+ "    ibc_transfer_hourly_cashflow as cashflow\n"
			+ "left join derivatives\n"
			+ "    on cashflow.zone = derivatives.zone and cashflow.denom = derivatives.denom\n"
			+ "where\n"
			+ "    derivatives.zone is NULL and derivatives.denom is NULL")
	Set<Cashflow> findUnmatchedCashflow();
}
