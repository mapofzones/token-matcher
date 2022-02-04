package com.mapofzones.tokenmatcher.service.zonenode;

import com.mapofzones.tokenmatcher.domain.ZoneNode;

import java.util.List;

public interface IZoneNodeService {

	List<ZoneNode> getAliveByName(String zoneName);
}
