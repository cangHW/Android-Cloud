package com.cloud.api.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by cangHX
 * on 2019/03/26  17:19
 */
class ThreadRuntime {

    private ExecutorService mService;

    private ThreadRuntime() {
        init();
    }

    private void init() {
        int max = ThreadManager.getInstance().getMaxRuntimeSize();
        mService = Executors.newFixedThreadPool(max);
    }

    private static class Factory {
        private static final ThreadRuntime mInstance = new ThreadRuntime();
    }

    static ThreadRuntime getInstance() {
        return Factory.mInstance;
    }

    void run(String key, Runnable runnable) {
        try {
            startTask(key, runnable);
        } catch (Throwable e) {
            if (mService == null || mService.isShutdown()) {
                init();
            }
            try {
                startTask(key, runnable);
            } catch (Throwable ignored) {
            }
        }
    }

    private void startTask(final String key, final Runnable runnable) {
        Future future = mService.submit(new Runnable() {
            @Override
            public void run() {
                runnable.run();
                ThreadPool.getInstance().remove(key);
            }
        });
        ThreadPool.getInstance().add(key, future);
    }

    void shutdownNow() {
        mService.shutdownNow();
        ThreadPool.getInstance().clear();
    }
}
