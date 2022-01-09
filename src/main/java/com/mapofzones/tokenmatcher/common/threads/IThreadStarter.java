package com.mapofzones.tokenmatcher.common.threads;

public interface IThreadStarter {

    void startThreads(Runnable function);
    void waitMainThread();
    boolean isDone();

}
