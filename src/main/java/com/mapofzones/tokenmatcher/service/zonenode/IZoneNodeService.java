package com.mapofzones.tokenmatcher.service.zonenode;

import com.mapofzones.tokenmatcher.domain.ZoneNode;

public interface IZoneNodeService {

	public ZoneNode getAliveByName(String zoneName);
}
