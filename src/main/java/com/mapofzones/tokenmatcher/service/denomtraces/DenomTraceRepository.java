package com.mapofzones.tokenmatcher.service.denomtraces;

import com.mapofzones.tokenmatcher.domain.DenomTrace;
import com.mapofzones.tokenmatcher.service.base.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DenomTraceRepository extends GenericRepository<DenomTrace, String> {



}
