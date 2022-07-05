package com.mapofzones.tokenmatcher.service;

import com.mapofzones.tokenmatcher.common.threads.IThreadStarter;
import com.mapofzones.tokenmatcher.domain.Derivative;
import com.mapofzones.tokenmatcher.domain.token.Token;
import com.mapofzones.tokenmatcher.domain.Zone;
import com.mapofzones.tokenmatcher.domain.token.TokenId;
import com.mapofzones.tokenmatcher.service.derivative.IDerivativeService;
import com.mapofzones.tokenmatcher.service.token.ITokenService;
import com.mapofzones.tokenmatcher.service.zone.IZoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class PathfinderFacade {

    private final ITokenService abstractTokenService;
    private final IZoneService zoneService;
    private final IDerivativeService derivativeService;
    private final IThreadStarter pathfinderThreadStarter;

    private BlockingQueue<Derivative> derivativeQueue;

    public PathfinderFacade(ITokenService abstractTokenService,
                            IZoneService zoneService,
                            IDerivativeService derivativeService,
                            @Qualifier("pathfinderThreadStarter") IThreadStarter pathfinderThreadStarter) {
        this.abstractTokenService = abstractTokenService;
        this.zoneService = zoneService;
        this.derivativeService = derivativeService;
        this.pathfinderThreadStarter = pathfinderThreadStarter;
    }

    @Async
    public void findAll() {
        List<Derivative> incompleteDerivativeList = derivativeService.findIncomplete();
        if (!incompleteDerivativeList.isEmpty()) {
            derivativeQueue = new ArrayBlockingQueue<>(incompleteDerivativeList.size(), true, incompleteDerivativeList);
            pathfinderThreadStarter.startThreads(pathfinderFunction);
        }
    }

    @Transactional
    public void find(Derivative derivative) {
        Token token = new Token();
        String baseDenom = extractBaseDenom(derivative.getDerivativeId().getFullDenom());

        if (derivative.getDerivativeId().getFullDenom().startsWith("transfer")) {

            Iterator<String> channelTrace = getChannelTrace(derivative.getDerivativeId().getFullDenom());
            String originZone = findOriginZone(derivative.getDerivativeId().getZone(), channelTrace);

            token.setTokenId(new TokenId(originZone, baseDenom));

            if (originZone.isBlank()) {
                return;
            }

        } else {
            token.setTokenId(new TokenId(derivative.getDerivativeId().getZone(), baseDenom));
        }

        abstractTokenService.saveToken(token);
        derivativeService.setTokenIdData(derivative.getDerivativeId(), token.getTokenId());
    }

    private final Runnable pathfinderFunction = () -> derivativeQueue.stream().parallel().forEach(node -> {
        try {
            Derivative currentDerivative = derivativeQueue.take();
            find(currentDerivative);
        } catch (InterruptedException e) {
            log.error("Queue error. " + e.getCause());
            Thread.currentThread().interrupt();
        }
    });


    private String findOriginZone(String zone, Iterator<String> channelIterator) {
        if (channelIterator.hasNext()) {
            String currentChannel = channelIterator.next();
            Zone foundZone = zoneService.findOriginZone(zone, currentChannel);

            if (foundZone != null)
                return findOriginZone(foundZone.getChainId(), channelIterator);
            else return "";

        } else {
            return zone;
        }
    }

    private Iterator<String> getChannelTrace(String fullDenom) {

        Matcher methodMatcher = Pattern.compile("channel-[\\d]*").matcher(fullDenom);
        List<String> channels = new ArrayList<>();

        int endIndex = 0;
        for (int i = 0; i <= fullDenom.length(); i++) {
            if (methodMatcher.find(endIndex)) {
                channels.add(methodMatcher.group());
                endIndex = methodMatcher.end();
            }
        }
        return channels.iterator();
    }

    private String extractBaseDenom(String fullDenom) {
        return fullDenom.substring(fullDenom.lastIndexOf("/") + 1);
    }
}
