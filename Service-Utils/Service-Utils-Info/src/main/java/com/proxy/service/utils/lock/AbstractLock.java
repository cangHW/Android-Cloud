package com.proxy.service.utils.lock;

import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.pool.thread.ThreadManager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : cangHX
 * on 2020/08/07  10:30 PM
 */
public abstract class AbstractLock {

    protected static class Info {
        public AtomicInteger atomicInteger;
        public Runnable runnable;
    }

    private static final LinkedBlockingQueue<Info> INFO_QUEUE = new LinkedBlockingQueue<>();
    private static final ArrayBlockingQueue<Integer> INTEGERS = new ArrayBlockingQueue<>(1);
    private static final AtomicBoolean IS_RUNNING = new AtomicBoolean(false);

    protected boolean add(Info info) {
        if (INFO_QUEUE.size() >= Integer.MAX_VALUE) {
            Logger.Error(CloudApiError.DATA_TO_MORE.build());
            return false;
        }
        try {
            INFO_QUEUE.put(info);
        } catch (InterruptedException e) {
            Logger.Error(e);
            return false;
        }
        INTEGERS.offer(1);
        loop();
        return true;
    }

    protected void loop() {
        if (!IS_RUNNING.compareAndSet(false, true)) {
            return;
        }
        Integer id = INTEGERS.poll();
        if (id == null) {
            IS_RUNNING.set(false);
            return;
        }
        ThreadManager.postWork(new Runnable() {
            @Override
            public void run() {
                while (INFO_QUEUE.size() != 0) {
                    try {
                        Info info = INFO_QUEUE.poll(1, TimeUnit.SECONDS);
                        if (info == null) {
                            continue;
                        }
                        AtomicInteger atomicInteger = info.atomicInteger;
                        if (atomicInteger == null) {
                            //数据缺失，抛弃本次回调
//                            Runnable runnable = info.runnable;
//                            if (runnable != null) {
//                                runnable.run();
//                            }
                            continue;
                        }
                        if (atomicInteger.get() > 0) {
                            INFO_QUEUE.put(info);
                            continue;
                        }
                        Runnable runnable = info.runnable;
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
