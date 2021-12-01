package com.mapofzones.tokenmatcher.common.threads;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mapofzones.tokenmatcher.domain.Cashflow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Slf4j
@Service
public class ThreadStarter implements IThreadStarter{

    public void startThreads(Runnable function, Integer threadCount, String threadNaming) {
        ExecutorService executorService = createThreadPool(threadNaming, threadCount);

        List<CompletableFuture<Void>> cfList = run(function, threadCount, executorService);
        waitAllThreads(cfList);
        executorService.shutdown();
    }

    private ExecutorService createThreadPool(String threadNaming, Integer threadCount) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(threadNaming)
                .build();

        return Executors.newFixedThreadPool(threadCount, threadFactory);
    }

    private List<CompletableFuture<Void>> run(Runnable function, Integer threadCount, ExecutorService executorService) {
        List<CompletableFuture<Void>> cfList = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {

            CompletableFuture<Void> cf = CompletableFuture
                    .runAsync(function, executorService)
                    .thenRun(() -> log.info(Thread.currentThread().getName() + " is finished"));

            cfList.add(cf);
        }
        return cfList;
    }


    private void waitAllThreads(List<CompletableFuture<Void>> cfList) {
        cfList.forEach(cf -> {
            try {
                cf.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("Thread error. " + e.getCause());
                e.printStackTrace();
            } finally {
                cf.cancel(false);
            }
        });
    }

}
