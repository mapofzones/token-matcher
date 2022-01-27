package com.mapofzones.tokenmatcher.service.derivative;

import com.mapofzones.tokenmatcher.domain.Derivative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DerivativeRepository extends JpaRepository<Derivative, Derivative.DerivativeId> {

	@Modifying
	@Query(value = "INSERT INTO derivatives (zone, full_denom) VALUES (?1, ?2) ON CONFLICT DO NOTHING", nativeQuery = true)
	void save(String zone, String fullDenom);

	List<Derivative> findAllByBaseDenomIsNullAndOriginZoneIsNull();

}
