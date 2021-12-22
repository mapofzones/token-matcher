package com.mapofzones.tokenmatcher.service.ibcchannel;

import com.mapofzones.tokenmatcher.common.exceptions.EntityNotFoundException;
import com.mapofzones.tokenmatcher.domain.IbcChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IbcChanelService implements IIbcChanelService {

    private final IbcChannelRepository ibcChannelRepository;

    public IbcChanelService(IbcChannelRepository ibcChannelRepository) {
        this.ibcChannelRepository = ibcChannelRepository;
    }

    @Override
    public IbcChannel findById(IbcChannel.IbcChannelId channelId) {
        return ibcChannelRepository.findById(channelId).orElseThrow(() -> new EntityNotFoundException(channelId.toString()));
    }
}
