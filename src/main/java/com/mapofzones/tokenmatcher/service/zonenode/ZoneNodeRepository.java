package com.mapofzones.tokenmatcher.service.zonenode;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mapofzones.tokenmatcher.domain.ZoneNode;

@Repository
public interface ZoneNodeRepository extends JpaRepository<ZoneNode, String> {

	Optional<ZoneNode> findFirstByZoneAndIsLcdAddressActiveIsTrue(String zone);
	
}
