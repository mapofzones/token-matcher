package com.mapofzones.tokenmatcher.service.derivative.client;


import com.mapofzones.tokenmatcher.AbstractTest;
import com.mapofzones.tokenmatcher.config.DenomTraceClientConfig;
import com.mapofzones.tokenmatcher.config.PropertiesConfig;
import com.mapofzones.tokenmatcher.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureWebClient
@ContextConfiguration(classes = {DenomTraceClientConfig.class, TestConfig.class, PropertiesConfig.class})
public class DenomTraceClientTest extends AbstractTest {

    private static final String COSMOSHUB_LCD = "http://159.203.104.207:1317";
    private static final String OSMOSIS_LCD = "http://35.235.89.254:1317";
    private static final String AKASHNET_LCD = "http://3.235.249.94:1317";

    @Autowired
    private DenomTraceClient denomTraceClient;

    @Test
    public void findDenomTrace_IsSuccessfulReceived_Test() {
        DenomTraceDto receivedDto1 = denomTraceClient.findDenomTrace(COSMOSHUB_LCD, "14F9BC3E44B8A9C1BE1FB08980FAB87034C9905EF17CF2F5008FC085218811CC");
        DenomTraceDto receivedDto2 = denomTraceClient.findDenomTrace(OSMOSIS_LCD, "1DCC8A6CB5689018431323953344A9F6CC4D0BFB261E88C9F7777372C10CD076");
        DenomTraceDto receivedDto3 = denomTraceClient.findDenomTrace(AKASHNET_LCD, "27394FB092D2ECCD56123C74F36E4C1F926001CEADA9CA97EA622B25F41E5EB2");


        DenomTraceDto dto1 = new DenomTraceDto("transfer/channel-141", "uosmo", true);
        DenomTraceDto dto2 = new DenomTraceDto("transfer/channel-8", "uregen", true);
        DenomTraceDto dto3 = new DenomTraceDto("transfer/channel-0", "uatom", true);

        assertAll(
                () -> assertEquals(receivedDto1, dto1),
                () -> assertEquals(receivedDto2, dto2),
                () -> assertEquals(receivedDto3, dto3)
        );
    }

    @Test
    public void findDenomTrace_IsFailReceived_Test() {
        DenomTraceDto receivedDto1 = denomTraceClient.findDenomTrace(COSMOSHUB_LCD, "1DCC8A6CB5689018431323953344A9F6CC4D0BFB261E88C9F7777372C10CD076");
        DenomTraceDto receivedDto2 = denomTraceClient.findDenomTrace(OSMOSIS_LCD, "27394FB092D2ECCD56123C74F36E4C1F926001CEADA9CF2F5008FC085218811CC");
        DenomTraceDto receivedDto3 = denomTraceClient.findDenomTrace(AKASHNET_LCD, "14F9BC3E44B8A9C1BE1FB08980FAB87034C9905EF17CF2F5008FC085218811CC");

        DenomTraceDto dto1 = new DenomTraceDto(false);
        DenomTraceDto dto2 = new DenomTraceDto(false);
        DenomTraceDto dto3 = new DenomTraceDto(false);

        assertAll(
                () -> assertEquals(receivedDto1.isSuccessReceived(), dto1.isSuccessReceived()),
                () -> assertEquals(receivedDto2.isSuccessReceived(), dto2.isSuccessReceived()),
                () -> assertEquals(receivedDto3.isSuccessReceived(), dto3.isSuccessReceived())
        );
    }
}