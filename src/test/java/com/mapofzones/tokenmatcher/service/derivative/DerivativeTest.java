package com.mapofzones.tokenmatcher.service.derivative;

import com.mapofzones.tokenmatcher.domain.Cashflow;
import com.mapofzones.tokenmatcher.domain.Derivative;
import com.mapofzones.tokenmatcher.domain.ZoneNode;
import com.mapofzones.tokenmatcher.service.derivative.client.DenomTraceClient;
import com.mapofzones.tokenmatcher.service.derivative.client.DenomTraceDto;
import com.mapofzones.tokenmatcher.service.zonenode.IZoneNodeService;
import com.mapofzones.tokenmatcher.service.zonenode.ZoneNodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class DerivativeTest {

    @Mock
    private DenomTraceClient denomTraceClient;
    @Mock
    private IZoneNodeService zoneNodeService;

    private IDerivativeService derivativeService;

    @BeforeEach
    public void setUp() {
        denomTraceClient = Mockito.mock(DenomTraceClient.class);
        zoneNodeService = Mockito.mock(ZoneNodeService.class);
        derivativeService = new DerivativeService(null, denomTraceClient, zoneNodeService);
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
    public void buildViaCashFlow_withIsReceivedOperation_Test() {
        Derivative.DerivativeId derivativeId = new Derivative.DerivativeId("columbus-5", "transfer/channel-1/transfer/channel-72/uluna");
        Derivative actualDerivative = new Derivative();
        actualDerivative.setDerivativeId(derivativeId);

        assertEquals(derivativeService.buildViaCashFlow(getCashflowWithReceivedOperaton()), actualDerivative);
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

    private static Cashflow getCashflowWithReceivedOperaton() {
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

}
