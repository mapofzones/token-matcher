package com.mapofzones.tokenmatcher.service.derivative;

import com.mapofzones.tokenmatcher.common.exceptions.EntityNotFoundException;
import com.mapofzones.tokenmatcher.domain.Cashflow;
import com.mapofzones.tokenmatcher.domain.Derivative;
import com.mapofzones.tokenmatcher.domain.IbcChannel;
import com.mapofzones.tokenmatcher.domain.Token;
import com.mapofzones.tokenmatcher.domain.ZoneNode;
import com.mapofzones.tokenmatcher.service.derivative.client.DenomTraceClient;
import com.mapofzones.tokenmatcher.service.derivative.client.DenomTraceDto;
import com.mapofzones.tokenmatcher.service.ibcchannel.IIbcChanelService;
import com.mapofzones.tokenmatcher.service.zonenode.IZoneNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@Slf4j
public class DerivativeService implements IDerivativeService {

	private final String EMPTY_STRING = "";

	private final DerivativeRepository derivativeRepository;
	private final DenomTraceClient denomTraceClient;
	private final IZoneNodeService zoneNodeService;
	private final IIbcChanelService ibcChanelService;

	public DerivativeService(DerivativeRepository derivativeRepository,
							 DenomTraceClient denomTraceClient,
							 IZoneNodeService zoneNodeService,
							 IIbcChanelService ibcChanelService) {
		this.derivativeRepository = derivativeRepository;
		this.denomTraceClient = denomTraceClient;
		this.zoneNodeService = zoneNodeService;
		this.ibcChanelService = ibcChanelService;
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
		derivative.setDerivativeId(new Derivative.DerivativeId(cashflow.getCashflowId().getZone(), EMPTY_STRING));

		if (denomIsHash(cashflow.getCashflowId().getDenom())) {
			setFullDenomByIbcHash(cashflow, derivative);
		} else  {
			derivative.getDerivativeId().setFullDenom(cashflow.getCashflowId().getDenom());
		}

		if (isReceived(cashflow)) {
			IbcChannel.IbcChannelId channelId = new IbcChannel.IbcChannelId(cashflow.getCashflowId().getZone(), cashflow.getCashflowId().getIbcChannel());
			String counterpartyChannel = findCounterpartyChannel(channelId);
			if (isCounterpartyChannel(derivative.getDerivativeId().getFullDenom(), counterpartyChannel)) {
				unescrow(derivative);
			} else if (counterpartyChannel != null){
				escrow(cashflow, derivative);
			} else derivative.setSuccessfulBuild(false);
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

	private boolean isReceived(Cashflow cashflow) {
		return cashflow.getCashflowId().getZone().equals(cashflow.getCashflowId().getZoneDestination());
	}

	private String findAddressByZone(String zone) {
		ZoneNode zoneNode = zoneNodeService.getAliveByName(zone);
		return zoneNode.getLcdAddress() != null ? zoneNode.getLcdAddress() : EMPTY_STRING;
	}

	private void setFullDenomByIbcHash(Cashflow cashflow, Derivative derivative) {
		String address = findAddressByZone(cashflow.getCashflowId().getZone());
		DenomTraceDto foundDto = denomTraceClient
				.findDenomTrace(address, cashflow.getCashflowId().getDenom().replace("ibc/", EMPTY_STRING));
		derivative.setDenomTraceData(foundDto);
	}

	private String findCounterpartyChannel(IbcChannel.IbcChannelId channelId) {
		String counterpartyChannel = ibcChanelService.findById(channelId).getCounterpartyChannelId();
		if (counterpartyChannel == null) {
			log.warn("Counterparty not found: " + channelId.toString());
		}
		return counterpartyChannel;
	}

	private boolean isCounterpartyChannel(String fullDenom, String counterpartyChannel) {
		String lastUsedChannelInFullDenom = findLastUsedChannelInDenom(fullDenom).replace("transfer/", EMPTY_STRING);
		return lastUsedChannelInFullDenom.equals(counterpartyChannel);
	}

	private void escrow(Cashflow cashflow, Derivative derivative) {
		String fullDenom = "transfer/" +
				cashflow.getCashflowId().getIbcChannel() + "/" +
				derivative.getDerivativeId().getFullDenom();
		derivative.getDerivativeId().setFullDenom(removeLastExtraSlash(fullDenom));
	}

	private void unescrow(Derivative derivative) {
		String lastUsedChannel = findLastUsedChannelInDenom(derivative.getDerivativeId().getFullDenom());
		String fullDenom = derivative.getDerivativeId().getFullDenom().replace(lastUsedChannel + "/", EMPTY_STRING);
		derivative.getDerivativeId().setFullDenom(fullDenom);
	}

	private String removeLastExtraSlash(String str) {
		return str.endsWith("/") ? str.substring(0, str.length() - 1) : str;
	}

	private String findLastUsedChannelInDenom(String denom) {
		Matcher methodMatcher = Pattern.compile("transfer/channel-[\\d]*").matcher(denom);

		List<String> channels = new ArrayList<>();
		while (methodMatcher.find())
			channels.add(methodMatcher.group());

		if (!channels.isEmpty())
			return channels.stream().skip(channels.size() - 1).findFirst().orElse(EMPTY_STRING);
		else return EMPTY_STRING;
	}
}