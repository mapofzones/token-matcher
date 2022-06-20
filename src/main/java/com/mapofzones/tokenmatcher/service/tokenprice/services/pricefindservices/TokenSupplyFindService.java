package com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices;

import com.mapofzones.tokenmatcher.domain.ZoneNode;
import com.mapofzones.tokenmatcher.service.tokenprice.client.TokenSupplyClient;
import com.mapofzones.tokenmatcher.service.tokenprice.services.pricefindservices.dto.TokenPriceDto;
import com.mapofzones.tokenmatcher.service.zonenode.IZoneNodeService;
import com.mapofzones.tokenmatcher.utils.TimeHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class TokenSupplyFindService extends AbstractPriceFindService {

    private final IZoneNodeService zoneNodeService;
    private final TokenSupplyClient tokenSupplyClient;

    public TokenSupplyFindService(TokenSupplyClient tokenSupplyClient,
                                  IZoneNodeService zoneNodeService) {
        super(tokenSupplyClient);
        this.tokenSupplyClient = tokenSupplyClient;
        this.zoneNodeService = zoneNodeService;
    }

    @Override
    public TokenPriceDto findTokenPrice(String tokenId, LocalDateTime lastTokenPriceTime) {

        String[] token = tokenId.split(":");
        String zone = token[0];
        String baseToken = token[1];

        Optional<ZoneNode> zoneNode = zoneNodeService.findAddressWithHightestBlockByChainId(zone);

        TokenPriceDto foundSupply;
        if (zoneNode.isPresent()) {
            foundSupply = tokenSupplyClient.findTokenPrice(zoneNode.get().getLcdAddress() + "%s/" + baseToken, lastTokenPriceTime).toTokenPrice();
            LocalDateTime firstTokenTime = foundSupply.getFirstPriceTime();
            if (firstTokenTime.isBefore(TimeHelper.nowAroundHours())) {
                completeTokenPrices(foundSupply);
            }
        } else foundSupply = new TokenPriceDto(Collections.emptyList());

        return foundSupply;
    }
}
