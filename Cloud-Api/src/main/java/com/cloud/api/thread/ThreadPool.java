package com.cloud.api.thread;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by cangHX
 * on 2019/03/26  17:03
 */
class ThreadPool {

    private HashMap<String, Future> mFutures = new HashMap<>();

    private ThreadPool() {
    }

    private static class Factory {
        private static final ThreadPool mInstance = new ThreadPool();
    }

    static ThreadPool getInstance() {
        return Factory.mInstance;
    }

    void add(String key, Future future) {
        mFutures.put(key, future);
    }

    private Future get(String key) {
        Future future = mFutures.get(key);
        if (future != null && future.isCancelled()) {
            remove(key);
        }
        return future;
    }

    void remove(String key) {
        mFutures.remove(key);
        checkIsCancel();
    }

    void cancel(String key) {
        Future future = get(key);
        if (future == null) {
            return;
        }
        if (future.isCancelled()) {
            remove(key);
            return;
        }
        future.cancel(true);
    }

    private void checkIsCancel() {
        for (Map.Entry<String, Future> entry : new HashMap<>(mFutures).entrySet()) {
            Future future = entry.getValue();
            if (future != null && future.isCancelled()) {
                mFutures.remove(entry.getKey());
            }
        }
    }

    void clear() {
        mFutures.clear();
    }
}
