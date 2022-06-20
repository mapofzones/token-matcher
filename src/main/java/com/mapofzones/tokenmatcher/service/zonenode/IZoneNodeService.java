package com.mapofzones.tokenmatcher.service.zonenode;

import com.mapofzones.tokenmatcher.domain.ZoneNode;

import java.util.List;
import java.util.Optional;

public interface IZoneNodeService {

	Optional<ZoneNode> findAddressWithHightestBlockByChainId(String chainId);
	List<ZoneNode> getAliveByName(String zoneName);
}
