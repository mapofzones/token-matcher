package com.mapofzones.tokenmatcher.service.derivative;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mapofzones.tokenmatcher.common.exceptions.EntityNotFoundException;
import com.mapofzones.tokenmatcher.common.exceptions.ExceptionMessages;
import com.mapofzones.tokenmatcher.domain.Derivative;
import com.mapofzones.tokenmatcher.service.derivative.client.DenomTraceClient;
import com.mapofzones.tokenmatcher.service.derivative.client.DenomTraceDto;
import com.mapofzones.tokenmatcher.service.zonenode.IZoneNodeService;

@Service
@Transactional
public class DerivativeService implements IDerivativeService {
	
	private DerivativeRepository derivativeRepository;
	private DenomTraceClient denomTraceClient;
	private IZoneNodeService zoneNodeService;

	public DerivativeService(DerivativeRepository derivativeRepository, 
			DenomTraceClient denomTraceClient,
			IZoneNodeService zoneNodeService) {
		this.derivativeRepository = derivativeRepository;
		this.denomTraceClient = denomTraceClient;
		this.zoneNodeService = zoneNodeService;
	}

	public Set<Derivative> findUnmatchedDerivatives() {
		return derivativeRepository.findByPathIsEmpty();
	}

	@Override
	public Derivative match(Derivative.DerivativeId id) {
		Derivative unmatched = derivativeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.ERROR_ENTITY_NOT_FOUND, id.toString()));
		
		// TODO: Need refactoring
		String denom = id.getDenom();
		if (denomIsHash(id.getDenom())) {
			String address = zoneNodeService.getAliveByName(id.getZone()).getRpcAddress();
			DenomTraceDto foundDto = denomTraceClient.findDenomTrace(address, id.getDenom().replace("ibc/", ""));
			unmatched.merge(foundDto);
		} else if (denomIsTransfer(id.getDenom())) {
			unmatched.setPath(denom.substring(0, denom.lastIndexOf("/")));
			unmatched.setBaseDenom(denom.substring(denom.lastIndexOf("/") + 1, denom.length()));
		} else {
			unmatched.setBaseDenom(denom);
		}
		return unmatched;
	}
	
	private boolean denomIsHash(String denom) {
		return denom.contains("ibc/");
	}
	
	private boolean denomIsTransfer(String denom) {
		return denom.contains("transfer/");
	}
}