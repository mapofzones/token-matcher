package com.mapofzones.tokenmatcher.service.zonenode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mapofzones.tokenmatcher.common.exceptions.EntityNotFoundException;
import com.mapofzones.tokenmatcher.domain.ZoneNode;

@Service
@Transactional(readOnly = true)
public class ZoneNodeService implements IZoneNodeService {

	private final ZoneNodeRepository zoneNodeRepository;
		
	public ZoneNodeService(ZoneNodeRepository zoneNodeRepository) {
		this.zoneNodeRepository = zoneNodeRepository;
	}

	@Override
	public ZoneNode getAliveByName(String zoneName) {
		return zoneNodeRepository.findFirstByZoneAndIsAliveIsTrue(zoneName)
				.orElseThrow(() -> new EntityNotFoundException("Not found ZoneNode", zoneName));
	}
}
