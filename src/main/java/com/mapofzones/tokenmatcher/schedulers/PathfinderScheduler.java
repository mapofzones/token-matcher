package com.mapofzones.tokenmatcher.schedulers;

import com.mapofzones.tokenmatcher.common.threads.IThreadStarter;
import com.mapofzones.tokenmatcher.service.PathfinderFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PathfinderScheduler {

    private final PathfinderFacade pathfinderFacade;
    private final IThreadStarter pathfinderThreadStarter;

    public PathfinderScheduler(PathfinderFacade pathfinderFacade,
                               IThreadStarter pathfinderThreadStarter) {
        this.pathfinderFacade = pathfinderFacade;
        this.pathfinderThreadStarter = pathfinderThreadStarter;
    }

    @Scheduled(fixedDelayString = "#{pathfinderProperties.syncTime}")
    public void run() {
//        if (pathfinderThreadStarter.isDone()) {
//            log.info("Pathfinder is running.");
//            pathfinderFacade.findAll();
//        }
    }
}
