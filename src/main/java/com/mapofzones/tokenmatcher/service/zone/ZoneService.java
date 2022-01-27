package com.mapofzones.tokenmatcher.service.zone;

import com.mapofzones.tokenmatcher.domain.Zone;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ZoneService implements IZoneService {

    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @Override
    public Zone findOriginZone(String zoneName, String channel) {
        return zoneRepository.findZoneByChannelAndZoneName(zoneName, channel);
    }
}
