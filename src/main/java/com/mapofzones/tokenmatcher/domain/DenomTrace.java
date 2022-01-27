package com.mapofzones.tokenmatcher.domain;

import com.mapofzones.tokenmatcher.service.denomtraces.client.DenomTraceDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Entity
@Table(name = "denom_traces")
@NoArgsConstructor
public class DenomTrace {

    public DenomTrace(DenomTraceDto dto) {
        setDenomTraceData(dto);
    }

    public DenomTrace(boolean isSuccessfulReceived) {
        this.isSuccessfulReceived = isSuccessfulReceived;
    }

    @Id
    @Column(name = "hash")
    private String hash;

    @Column(name = "path")
    private String path;

    @Column(name = "base_denom")
    private String baseDenom;

    @Transient
    private boolean isSuccessfulReceived = true;

    public void setDenomTraceData(DenomTraceDto dto) {
        this.hash = dto.getIbcHash();
        this.path = dto.getPath();
        this.baseDenom = dto.getBaseDenom();
        this.isSuccessfulReceived = dto.isSuccessReceived();
    }

}
