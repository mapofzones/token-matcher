package com.mapofzones.tokenmatcher.service.zone;

import com.mapofzones.tokenmatcher.domain.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, String> {

    @Query(nativeQuery = true,
            value = "SELECT z.chain_id FROM zones z " +
            "JOIN ibc_clients ibc_cl ON z.chain_id = ibc_cl.chain_id " +
            "JOIN ibc_connections ibc_conn ON ibc_cl.zone = ibc_conn.zone AND ibc_cl.client_id = ibc_conn.client_id " +
            "JOIN ibc_channels ibc_chan ON ibc_conn.zone = ibc_chan.zone AND ibc_conn.connection_id = ibc_chan.connection_id " +
            "WHERE ibc_chan.zone = ?1 AND ibc_chan.channel_id = ?2")
    Zone findZoneByChannelAndZoneName(String zoneName, String channel);

}
