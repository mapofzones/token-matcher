package com.mapofzones.tokenmatcher.service.tokenprice.services;

import com.mapofzones.tokenmatcher.domain.tokenprice.TokenSupply;
import com.mapofzones.tokenmatcher.service.tokenprice.repository.SupplyRepository;
import com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices.TokenSupplyFindService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class TokenSupplyService extends AbstractTokenPriceService<TokenSupplyFindService, SupplyRepository, TokenSupply> {

    public TokenSupplyService(TokenSupplyFindService tokenSupplyFindService,
                              SupplyRepository supplyRepository,
                              TokenSupply tokenSupply) {
        super(tokenSupplyFindService, supplyRepository, tokenSupply);
    }

}
