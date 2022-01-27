package com.mapofzones.tokenmatcher.service.denomtraces;

import com.mapofzones.tokenmatcher.domain.DenomTrace;
import com.mapofzones.tokenmatcher.service.base.IGenericService;

public interface IDenomTraceService extends IGenericService<DenomTrace, String, DenomTraceRepository> {

    DenomTrace findByIbcHashViaRestApi(String address, String hash);
    DenomTrace findByIbcHashViaCache(String hash);

}
