package com.proxy.service.utils.pool.thread;

import com.proxy.service.utils.pool.thread.runtime.ExecutorCore;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 线程管理类
 *
 * @author : cangHX
 * on 2020/07/30  6:37 PM
 */
public class ThreadManager {

    public static void postMain(Runnable runnable) {
        ExecutorCore.INSTANCE.getMainThread().post(runnable);
    }

    public static void postWork(Runnable runnable) {
        ExecutorCore.INSTANCE.getWorkExecutorService().submit(runnable);
    }

    public static void postCurrent(Runnable runnable) {
        ExecutorCore.INSTANCE.getImmediateExecutor().execute(runnable);
    }

    public static void postScheduled(Runnable runnable, long timeOut, TimeUnit unit) {
        ExecutorCore.INSTANCE.getScheduledExecutorService().schedule(runnable, timeOut, unit);
    }

}
