package com.mapofzones.tokenmatcher.service.tokenprice.services;

import com.mapofzones.tokenmatcher.domain.tokenprice.OsmosisTokenPrice;
import com.mapofzones.tokenmatcher.service.tokenprice.repository.IOsmosisRepository;
import com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices.OsmosisPriceFindService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class OsmosisTokenPriceService extends AbstractTokenPriceService<OsmosisPriceFindService, IOsmosisRepository, OsmosisTokenPrice> {

    public OsmosisTokenPriceService(OsmosisPriceFindService osmosisPriceFindService,
                                    IOsmosisRepository osmosisRepository,
                                    OsmosisTokenPrice osmosisTokenPriceCreator) {
        super(osmosisPriceFindService, osmosisRepository, osmosisTokenPriceCreator);
    }

}
