package com.mapofzones.tokenmatcher.service.derivative;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mapofzones.tokenmatcher.domain.Derivative;

@Repository
public interface DerivativeRepository extends JpaRepository<Derivative, Derivative.DerivativeId> {

	@Query("FROM Derivative WHERE path = '' OR path IS NULL AND baseDenom = '' OR baseDenom IS NULL")
	Set<Derivative> findByPathIsEmpty();
}
