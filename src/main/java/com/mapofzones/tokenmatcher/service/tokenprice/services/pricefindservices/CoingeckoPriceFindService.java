package com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices;

import com.mapofzones.tokenmatcher.service.tokenprice.client.CoingeckoClient;
import org.springframework.stereotype.Service;

@Service
public class CoingeckoPriceFindService extends AbstractPriceFindService {

    public CoingeckoPriceFindService(CoingeckoClient coingeckoClient) {
        super(coingeckoClient);
    }
}
