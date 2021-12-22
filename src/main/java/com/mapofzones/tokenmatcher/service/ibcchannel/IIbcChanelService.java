package com.mapofzones.tokenmatcher.service.ibcchannel;

import com.mapofzones.tokenmatcher.domain.IbcChannel;

public interface IIbcChanelService {

    IbcChannel findById(IbcChannel.IbcChannelId channelId);
}
