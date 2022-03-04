package com.mapofzones.tokenmatcher.service.tokenprice.services;

import com.mapofzones.tokenmatcher.domain.tokenprice.CoingeckoTokenPrice;
import com.mapofzones.tokenmatcher.service.tokenprice.repository.ICoingeckoRepository;
import com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices.CoingeckoPriceFindService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class CoingeckoTokenPriceService extends AbstractTokenPriceService<CoingeckoPriceFindService, ICoingeckoRepository, CoingeckoTokenPrice> {

    public CoingeckoTokenPriceService(CoingeckoPriceFindService coingeckoPriceFindService,
                                      ICoingeckoRepository coingeckoRepository,
                                      CoingeckoTokenPrice coingeckoTokenPriceCreator) {
        super(coingeckoPriceFindService, coingeckoRepository, coingeckoTokenPriceCreator);
    }
}
