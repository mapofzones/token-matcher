package com.mapofzones.tokenmatcher.service.zonenode;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mapofzones.tokenmatcher.domain.ZoneNode;

@Repository
public interface ZoneNodeRepository extends JpaRepository<ZoneNode, String> {

	// TODO: Refactoring for 3-tables version of service
	@Query(value =
			"SELECT * from zone_nodes zn " +
			"    WHERE zn.zone = ?1 " +
			"        AND zn.last_block_height is not null " +
			"        AND zn.is_lcd_addr_active = true order by zn.last_block_height DESC LIMIT 1", nativeQuery = true)
	Optional<ZoneNode> findAddressWithHightestBlockByChainId(String chainId);

	List<ZoneNode> findAllByZoneAndIsLcdAddressActiveIsTrue(String zone);
	
}
