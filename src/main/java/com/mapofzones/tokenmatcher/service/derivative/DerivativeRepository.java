package com.mapofzones.tokenmatcher.service.derivative;

import com.mapofzones.tokenmatcher.domain.Derivative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DerivativeRepository extends JpaRepository<Derivative, Derivative.DerivativeId> {

	List<Derivative> findAllByBaseDenomIsNullAndOriginZoneIsNull();

}
