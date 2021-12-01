package com.mapofzones.tokenmatcher.service;

import com.mapofzones.tokenmatcher.common.properties.PathfinderProperties;
import com.mapofzones.tokenmatcher.common.threads.IThreadStarter;
import com.mapofzones.tokenmatcher.domain.Derivative;
import com.mapofzones.tokenmatcher.domain.Token;
import com.mapofzones.tokenmatcher.domain.Zone;
import com.mapofzones.tokenmatcher.service.derivative.IDerivativeService;
import com.mapofzones.tokenmatcher.service.token.ITokenService;
import com.mapofzones.tokenmatcher.service.zone.IZoneService;
import lombok.extern.slf4j.Slf4j;
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

    private final PathfinderProperties properties;
    private final ITokenService tokenService;
    private final IZoneService zoneService;
    private final IDerivativeService derivativeService;
    private final IThreadStarter threadStarter;

    private BlockingQueue<Derivative> derivativeQueue;

    public PathfinderFacade(PathfinderProperties properties,
                            ITokenService tokenService,
                            IZoneService zoneService,
                            IDerivativeService derivativeService,
                            IThreadStarter threadStarter) {
        this.properties = properties;
        this.tokenService = tokenService;
        this.zoneService = zoneService;
        this.derivativeService = derivativeService;
        this.threadStarter = threadStarter;
    }

    public void findAll() {
        List<Derivative> incompleteDerivativeList = derivativeService.findIncomplete();
        derivativeQueue = new ArrayBlockingQueue<>(incompleteDerivativeList.size(), true, incompleteDerivativeList);
        threadStarter.startThreads(pathfinderFunction, properties.getThreads(), properties.getThreadsNaming());
    }

    @Transactional
    public void find(Derivative derivative) {
        Token token = new Token();
        String baseDenom = extractBaseDenom(derivative.getDerivativeId().getFullDenom());

        if (derivative.getDerivativeId().getFullDenom().startsWith("transfer")) {

            Iterator<String> channelTrace = getChannelTrace(derivative.getDerivativeId().getFullDenom());
            String originZone = findOriginZone(derivative.getDerivativeId().getZone(), channelTrace);

            token.setTokenId(new Token.TokenId(originZone, baseDenom));

            if (originZone.isEmpty()) {
                return;
            }

        } else {
            token.setTokenId(new Token.TokenId(derivative.getDerivativeId().getZone(), baseDenom));
        }

        tokenService.save(token);
        derivativeService.setTokenIdData(derivative.getDerivativeId(), token.getTokenId());
    }

    private final Runnable pathfinderFunction = () -> {
        while (true) {
            if (!derivativeQueue.isEmpty()) {
                try {
                    Derivative currentDerivative = derivativeQueue.take();
                    find(currentDerivative);
                } catch (InterruptedException e) {
                    log.error("Queue error. " + e.getCause());
                    e.printStackTrace();
                }
            }
            else break;
        }
    };

    private String findOriginZone(String zone, Iterator<String> channelIterator) {
        if (channelIterator.hasNext()) {
            String currentChannel = channelIterator.next();
            Zone foundZone = zoneService.findOriginZone(zone, currentChannel);

            if (foundZone != null)
                return findOriginZone(foundZone.getChainId(), channelIterator);
//            else if (!channelIterator.hasNext())
//                return zone;
            else
                return "";

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