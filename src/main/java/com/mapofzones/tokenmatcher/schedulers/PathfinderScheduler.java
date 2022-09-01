package com.mapofzones.tokenmatcher.schedulers;

import com.mapofzones.tokenmatcher.service.PathfinderFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"dev", "prod"})
public class PathfinderScheduler {

    private final PathfinderFacade pathfinderFacade;

    public PathfinderScheduler(PathfinderFacade pathfinderFacade) {
        this.pathfinderFacade = pathfinderFacade;
    }

    private int iteration = 1;

    @Scheduled(fixedDelayString = "#{pathfinderProperties.syncTime}", initialDelayString = "2000")
    public void run() {
        log.info("[{}] Pathfinder is running", iteration);
        pathfinderFacade.findAll();
        log.info("[{}] Pathfinder is finishing", iteration);
        log.info("-----------------------------------");
        iteration++;
    }
}
