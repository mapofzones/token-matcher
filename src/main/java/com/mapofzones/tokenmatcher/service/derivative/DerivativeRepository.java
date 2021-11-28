package com.mapofzones.tokenmatcher.service.derivative;

import com.mapofzones.tokenmatcher.domain.Derivative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DerivativeRepository extends JpaRepository<Derivative, Derivative.DerivativeId> {

	Boolean existsByDerivativeId(Derivative.DerivativeId derivativeId);

}
