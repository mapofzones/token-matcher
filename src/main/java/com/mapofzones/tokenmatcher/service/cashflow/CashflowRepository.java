package com.mapofzones.tokenmatcher.service.cashflow;

import com.mapofzones.tokenmatcher.domain.Cashflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashflowRepository extends JpaRepository<Cashflow, Cashflow.CashflowId>{

	List<Cashflow> findAllByDerivativeDenomIsNull();

}