package com.mapofzones.tokenmatcher.service.denomtraces;

import com.mapofzones.tokenmatcher.domain.DenomTrace;
import com.mapofzones.tokenmatcher.service.base.GenericService;
import com.mapofzones.tokenmatcher.service.denomtraces.client.DenomTraceClient;
import com.mapofzones.tokenmatcher.service.denomtraces.client.DenomTraceDto;
import org.springframework.stereotype.Service;

@Service
public class DenomTraceService extends GenericService<DenomTrace, String, DenomTraceRepository> implements IDenomTraceService {

    private final DenomTraceClient denomTraceClient;

    public DenomTraceService(DenomTraceRepository repository,
                             DenomTraceClient denomTraceClient) {
        super(repository);
        this.denomTraceClient = denomTraceClient;
    }

    @Override
    public DenomTrace findByIbcHashViaRestApi(String address, String hash) {
        DenomTraceDto dto = denomTraceClient.findDenomTrace(address, hash);
        return new DenomTrace(dto);
    }

    @Override
    public DenomTrace findByIbcHashViaCache(String hash) {
        return repository.findById(hash).orElse(new DenomTrace(false));
    }
}
