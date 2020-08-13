package com.proxy.service.utils.lock;

import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.pool.thread.ThreadManager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : cangHX
 * on 2020/08/07  10:30 PM
 */
public abstract class AbstractLock {

    protected static class LockInfo {
        public AtomicBoolean isLock;
        public Runnable task;
    }

    private static final LinkedBlockingQueue<LockInfo> LOCK_INFO_QUEUE = new LinkedBlockingQueue<>();
    private static final ArrayBlockingQueue<Integer> LOOP_QUEUE = new ArrayBlockingQueue<>(1);
    private static final AtomicBoolean IS_RUNNING = new AtomicBoolean(false);

    protected boolean add(LockInfo info) {
        if (LOCK_INFO_QUEUE.size() >= Integer.MAX_VALUE) {
            Logger.Error(CloudApiError.DATA_TO_MORE.build());
            return false;
        }
        try {
            LOCK_INFO_QUEUE.put(info);
        } catch (InterruptedException e) {
            Logger.Error(e);
            return false;
        }
        LOOP_QUEUE.offer(1);
        loop();
        return true;
    }

    private void loop() {
        if (!IS_RUNNING.compareAndSet(false, true)) {
            return;
        }
        Integer id = LOOP_QUEUE.poll();
        if (id == null) {
            IS_RUNNING.set(false);
            return;
        }
        ThreadManager.postWork(new Runnable() {
            @Override
            public void run() {
                while (LOCK_INFO_QUEUE.size() != 0) {
                    try {
                        LockInfo info = LOCK_INFO_QUEUE.poll(1, TimeUnit.SECONDS);
                        if (info == null) {
                            continue;
                        }
                        AtomicBoolean isLock = info.isLock;
                        if (isLock == null) {
                            //数据缺失，抛弃本次回调
//                            Runnable runnable = info.runnable;
//                            if (runnable != null) {
//                                runnable.run();
//                            }
                            continue;
                        }
                        if (isLock.get()) {
                            LOCK_INFO_QUEUE.put(info);
                            continue;
                        }
                        Runnable runnable = info.task;
                        if (runnable != null) {
                            runnable.run();
                        }
                    } catch (InterruptedException e) {
                        Logger.Debug(e);
                    }
                }
                IS_RUNNING.set(false);
                loop();
            }
        });
    }
}
