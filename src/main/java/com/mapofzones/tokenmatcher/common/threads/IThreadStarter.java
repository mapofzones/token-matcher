package com.mapofzones.tokenmatcher.common.threads;

public interface IThreadStarter {

    void startThreads(Runnable function, Integer threadCount, String threadNaming);

}
