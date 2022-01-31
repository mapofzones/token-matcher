package com.mapofzones.tokenmatcher.service.cashflow;

import com.mapofzones.tokenmatcher.domain.Cashflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashflowRepository extends JpaRepository<Cashflow, Cashflow.CashflowId>{

	@Query(value = "FROM Cashflow c WHERE c.derivativeDenom is null AND c.cashflowId.denom <> ''")
	List<Cashflow> findAllByDerivativeDenomIsNullAndDenomIsNotEmpty();

}