package com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices;

import com.mapofzones.tokenmatcher.service.tokenprice.client.OsmosisClient;
import org.springframework.stereotype.Service;

@Service
public class OsmosisPriceFindService extends AbstractPriceFindService {

    public OsmosisPriceFindService(OsmosisClient osmosisClient) {
        super(osmosisClient);
    }
}
