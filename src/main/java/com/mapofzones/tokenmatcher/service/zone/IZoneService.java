package com.mapofzones.tokenmatcher.service.zone;

import com.mapofzones.tokenmatcher.domain.Zone;

public interface IZoneService {

    Zone findOriginZone(String zoneName, String channel);

}
