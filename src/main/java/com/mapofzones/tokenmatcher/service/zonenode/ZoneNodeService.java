package com.mapofzones.tokenmatcher.service.zonenode;

import com.mapofzones.tokenmatcher.domain.ZoneNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ZoneNodeService implements IZoneNodeService {

	private final ZoneNodeRepository zoneNodeRepository;
		
	public ZoneNodeService(ZoneNodeRepository zoneNodeRepository) {
		this.zoneNodeRepository = zoneNodeRepository;
	}

	@Override
	public Optional<ZoneNode> findAddressWithHightestBlockByChainId(String chainId) {
		return zoneNodeRepository.findAddressWithHightestBlockByChainId(chainId);
	}

	@Override
	public List<ZoneNode> getAliveByName(String zoneName) {
		return zoneNodeRepository.findAllByZoneAndIsLcdAddressActiveIsTrue(zoneName);
	}
}
