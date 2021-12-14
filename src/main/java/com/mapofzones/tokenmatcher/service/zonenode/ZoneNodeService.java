package com.mapofzones.tokenmatcher.service.zonenode;

import com.mapofzones.tokenmatcher.common.exceptions.ExceptionMessages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mapofzones.tokenmatcher.common.exceptions.EntityNotFoundException;
import com.mapofzones.tokenmatcher.domain.ZoneNode;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ZoneNodeService implements IZoneNodeService {

	private final ZoneNodeRepository zoneNodeRepository;
		
	public ZoneNodeService(ZoneNodeRepository zoneNodeRepository) {
		this.zoneNodeRepository = zoneNodeRepository;
	}

	@Override
	public ZoneNode getAliveByName(String zoneName) {

		return zoneNodeRepository.findFirstByZoneAndIsLcdAddressActiveIsTrue(zoneName).orElse(new ZoneNode());
	}
}
