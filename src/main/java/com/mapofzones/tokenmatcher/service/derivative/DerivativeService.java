package com.mapofzones.tokenmatcher.service.derivative;

import com.mapofzones.tokenmatcher.common.exceptions.EntityNotFoundException;
import com.mapofzones.tokenmatcher.domain.Cashflow;
import com.mapofzones.tokenmatcher.domain.Derivative;
import com.mapofzones.tokenmatcher.domain.Token;
import com.mapofzones.tokenmatcher.domain.ZoneNode;
import com.mapofzones.tokenmatcher.service.derivative.client.DenomTraceClient;
import com.mapofzones.tokenmatcher.service.derivative.client.DenomTraceDto;
import com.mapofzones.tokenmatcher.service.zonenode.IZoneNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class DerivativeService implements IDerivativeService {
	
	private final DerivativeRepository derivativeRepository;
	private final DenomTraceClient denomTraceClient;
	private final IZoneNodeService zoneNodeService;

	public DerivativeService(DerivativeRepository derivativeRepository, 
			DenomTraceClient denomTraceClient,
			IZoneNodeService zoneNodeService) {
		this.derivativeRepository = derivativeRepository;
		this.denomTraceClient = denomTraceClient;
		this.zoneNodeService = zoneNodeService;
	}

	@Override
	public Derivative save(Derivative derivative) {
		return derivativeRepository.save(derivative);
	}

	@Override
	public List<Derivative> findIncomplete() {
		return derivativeRepository.findAllByBaseDenomIsNullAndOriginZoneIsNull();
	}

	@Override
	public Derivative buildViaCashFlow(Cashflow cashflow) {
		Derivative derivative = new Derivative();
		derivative.setDerivativeId(new Derivative.DerivativeId(cashflow.getCashflowId().getZone(), ""));

		String cashflowDenom = cashflow.getCashflowId().getDenom();

		if (denomIsHash(cashflow.getCashflowId().getDenom())) {
			String address = findAddressByZone(cashflow.getCashflowId().getZone());
			DenomTraceDto foundDto = denomTraceClient
					.findDenomTrace(address, cashflow.getCashflowId().getDenom().replace("ibc/", ""));
			derivative.setDenomTraceData(foundDto);
		} else  {
			derivative.getDerivativeId().setFullDenom(cashflowDenom);
		}

		if (isReceived(cashflow.getCashflowId().getZone(), cashflow.getCashflowId().getZoneDestination())) {
			String fullDenom = "transfer/" +
					cashflow.getCashflowId().getIbcChannel() + "/" +
					derivative.getDerivativeId().getFullDenom();

			if (fullDenom.endsWith("/")) {
				fullDenom = fullDenom.substring(0, fullDenom.length() - 1);
			}

			derivative.getDerivativeId().setFullDenom(fullDenom);
		}

		return derivative;
	}

	@Override
	public void setTokenIdData(Derivative.DerivativeId derivativeId, Token.TokenId tokenId) {
		Derivative derivative = derivativeRepository.findById(derivativeId)
				.orElseThrow(() -> new EntityNotFoundException(derivativeId.toString()));
		derivative.setTokenIdData(tokenId);
	}

	private boolean denomIsHash(String denom) {
		return denom.startsWith("ibc/");
	}

	private boolean isReceived(String zone, String zoneDestination) {
		return zone.equals(zoneDestination);
	}

	private String findAddressByZone(String zone) {
		ZoneNode zoneNode = zoneNodeService.getAliveByName(zone);
		return zoneNode.getLcdAddress() != null ? zoneNode.getLcdAddress() : "";
	}
}