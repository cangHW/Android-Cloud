package com.cloud.api.thread;

import java.util.Random;

/**
 * Created by cangHX
 * on 2019/03/26  17:19
 * <p>
 * 线程管理类
 */
public class ThreadManager {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //线程池最大线程数量
    private int mMaxRuntimeSize = CPU_COUNT + 1;

    private static class Factory {
        private static final ThreadManager mInstance = new ThreadManager();
    }

    public static ThreadManager getInstance() {
        return Factory.mInstance;
    }

    int getMaxRuntimeSize() {
        return mMaxRuntimeSize;
    }

    public String runTask(Runnable runnable) {
        String key = createKey();
        runTask(key, runnable);
        return key;
    }

    public void runTask(String key, Runnable runnable) {
        ThreadRuntime.getInstance().run(key, runnable);
    }

    public void cancelTask(String key) {
        ThreadPool.getInstance().cancel(key);
    }

    public void clear() {
        ThreadRuntime.getInstance().shutdownNow();
    }

    private String createKey() {
        long currentTime = System.currentTimeMillis();
        Random random = new Random();
        int time = random.nextInt();
        return currentTime + String.valueOf(time);
    }
}
