package com.mapofzones.tokenmatcher.service.derivative;

import com.mapofzones.tokenmatcher.domain.Cashflow;
import com.mapofzones.tokenmatcher.domain.Derivative;
import com.mapofzones.tokenmatcher.domain.IbcChannel;
import com.mapofzones.tokenmatcher.domain.ZoneNode;
import com.mapofzones.tokenmatcher.service.derivative.client.DenomTraceClient;
import com.mapofzones.tokenmatcher.service.derivative.client.DenomTraceDto;
import com.mapofzones.tokenmatcher.service.ibcchannel.IIbcChanelService;
import com.mapofzones.tokenmatcher.service.ibcchannel.IbcChanelService;
import com.mapofzones.tokenmatcher.service.zonenode.IZoneNodeService;
import com.mapofzones.tokenmatcher.service.zonenode.ZoneNodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class DerivativeTest {

    @Mock
    private DenomTraceClient denomTraceClient;
    @Mock
    private IZoneNodeService zoneNodeService;
    @Mock
    private IIbcChanelService ibcChanelService;

    private IDerivativeService derivativeService;

    @BeforeEach
    public void setUp() {
        denomTraceClient = Mockito.mock(DenomTraceClient.class);
        zoneNodeService = Mockito.mock(ZoneNodeService.class);
        ibcChanelService = Mockito.mock(IbcChanelService.class);
        derivativeService = new DerivativeService(null, denomTraceClient, zoneNodeService, ibcChanelService);
    }

    @Test
    public void buildViaCashFlow_withDenomIsHash_Test() {
        when(denomTraceClient.findDenomTrace(anyString(), anyString()))
                .thenReturn(new DenomTraceDto("transfer/channel-47/transfer/channel-18", "uluna", true));

        when(zoneNodeService.getAliveByName(anyString()))
                .thenReturn(new ZoneNode(null, null, null, "http://35.235.89.254:1317", null));

        Derivative.DerivativeId derivativeId = new Derivative.DerivativeId("osmosis-1", "transfer/channel-47/transfer/channel-18/uluna");
        Derivative actualDerivative = new Derivative();
        actualDerivative.setDerivativeId(derivativeId);

        assertEquals(derivativeService.buildViaCashFlow(getCashflowWithDenomIsHash()), actualDerivative);
    }

    @Test
    public void buildViaCashFlow_withDenomIsNotHash_Test() {
        Derivative.DerivativeId derivativeId = new Derivative.DerivativeId("cosmoshub-4", "uatom");
        Derivative actualDerivative = new Derivative();
        actualDerivative.setDerivativeId(derivativeId);

        assertEquals(derivativeService.buildViaCashFlow(getCashflowWithDenomIsNotHash()), actualDerivative);
    }

    @Test
    public void buildViaCashFlow_withIsReceivedOperationEscrow_Test() {
        when(ibcChanelService.findById(any())).thenReturn(getIbcChannel_1());

        Derivative.DerivativeId derivativeId = new Derivative.DerivativeId("columbus-5", "transfer/channel-1/transfer/channel-72/uluna");
        Derivative actualDerivative = new Derivative();
        actualDerivative.setDerivativeId(derivativeId);

        assertEquals(derivativeService.buildViaCashFlow(getCashflowWithReceivedOperation()), actualDerivative);
    }

    @Test
    public void buildViaCashFlow_withIsReceivedOperationUnescrow_Test() {
        when(ibcChanelService.findById(any())).thenReturn(getIbcChannel_2());

        Derivative.DerivativeId derivativeId = new Derivative.DerivativeId("columbus-5", "uluna");
        Derivative actualDerivative = new Derivative();
        actualDerivative.setDerivativeId(derivativeId);

        assertEquals(derivativeService.buildViaCashFlow(getCashflowWithReceivedOperation()), actualDerivative);
    }

    @Test
    public void buildViaCashFlow_withIsReceivedOperationCounterpartyIsNull_Test() {
        when(ibcChanelService.findById(any())).thenReturn(getIbcChannel_3());

        Derivative actualDerivative = new Derivative();
        actualDerivative.setSuccessfulBuild(false);

        assertEquals(derivativeService.buildViaCashFlow(getCashflowWithReceivedOperation()).isSuccessfulBuild(), actualDerivative.isSuccessfulBuild());
    }

    private static Cashflow getCashflowWithDenomIsHash() {
        Cashflow.CashflowId cashflowId = new Cashflow.CashflowId();
        cashflowId.setZone("osmosis-1");
        cashflowId.setZoneSource("osmosis-1");
        cashflowId.setZoneDestination("sifchain-1");
        cashflowId.setIbcChannel("channel-47");
        cashflowId.setDenom("ibc/963C2E3AD9772A9225F173E95815EFA8E64A7F9402A1902198624221D0FF51DB");
        cashflowId.setHour(LocalDateTime.now());
        cashflowId.setPeriod(1);

        Cashflow cashflow = new Cashflow();
        cashflow.setCashflowId(cashflowId);

        return cashflow;
    }

    private static Cashflow getCashflowWithDenomIsNotHash() {
        Cashflow.CashflowId cashflowId = new Cashflow.CashflowId();
        cashflowId.setZone("cosmoshub-4");
        cashflowId.setZoneSource("cosmoshub-4");
        cashflowId.setZoneDestination("osmosis-1");
        cashflowId.setIbcChannel("channel-141");
        cashflowId.setDenom("uatom");
        cashflowId.setHour(LocalDateTime.now());
        cashflowId.setPeriod(1);

        Cashflow cashflow = new Cashflow();
        cashflow.setCashflowId(cashflowId);

        return cashflow;
    }

    private static Cashflow getCashflowWithReceivedOperation() {
        Cashflow.CashflowId cashflowId = new Cashflow.CashflowId();
        cashflowId.setZone("columbus-5");
        cashflowId.setZoneSource("osmosis-1");
        cashflowId.setZoneDestination("columbus-5");
        cashflowId.setIbcChannel("channel-1");
        cashflowId.setDenom("transfer/channel-72/uluna");
        cashflowId.setHour(LocalDateTime.now());
        cashflowId.setPeriod(1);

        Cashflow cashflow = new Cashflow();
        cashflow.setCashflowId(cashflowId);

        return cashflow;
    }

    private static IbcChannel getIbcChannel_1() {
        IbcChannel.IbcChannelId ibcChannelId = new IbcChannel.IbcChannelId("columbus-5", "channel-0");

        IbcChannel ibcChannel = new IbcChannel();
        ibcChannel.setIbcChannelId(ibcChannelId);
        ibcChannel.setCounterpartyChannelId("channel-1");
        return ibcChannel;
    }

    private static IbcChannel getIbcChannel_2() {
        IbcChannel.IbcChannelId ibcChannelId = new IbcChannel.IbcChannelId("columbus-5", "channel-0");

        IbcChannel ibcChannel = new IbcChannel();
        ibcChannel.setIbcChannelId(ibcChannelId);
        ibcChannel.setCounterpartyChannelId("channel-72");
        return ibcChannel;
    }

    private static IbcChannel getIbcChannel_3() {
        IbcChannel.IbcChannelId ibcChannelId = new IbcChannel.IbcChannelId("columbus-5", "channel-0");

        IbcChannel ibcChannel = new IbcChannel();
        ibcChannel.setIbcChannelId(ibcChannelId);
        ibcChannel.setCounterpartyChannelId(null);
        return ibcChannel;
    }

}
