package com.mapofzones.tokenmatcher.service.ibcchannel;

import com.mapofzones.tokenmatcher.domain.IbcChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IbcChannelRepository extends JpaRepository<IbcChannel, IbcChannel.IbcChannelId> {

}
