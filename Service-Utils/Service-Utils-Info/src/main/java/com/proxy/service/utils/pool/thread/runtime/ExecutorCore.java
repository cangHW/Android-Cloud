package com.proxy.service.utils.pool.thread.runtime;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池核心
 *
 * @author : cangHX
 * on 2020/08/07  11:34 PM
 */
public class ExecutorCore {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE_TIME = 1L;

    public static ExecutorCore INSTANCE = new ExecutorCore();

    /**
     * UI 线程
     */
    private final Handler MAIN_THREAD = new Handler(Looper.getMainLooper());
    /**
     * 工作线程
     */
    private final ThreadPoolExecutor WORK_EXECUTOR_SERVICE;
    /**
     * 延时线程
     */
    private final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE;
    /**
     * 即时线程
     */
    private final Executor IMMEDIATE_EXECUTOR;

    private ExecutorCore() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new DefaultThreadFactory());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        WORK_EXECUTOR_SERVICE = threadPoolExecutor;

        SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

        IMMEDIATE_EXECUTOR = new ImmediateExecutor();
    }

    /**
     * 获取 UI 线程
     *
     * @return UI 线程
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/8 10:12 AM
     */
    public Handler getMainThread() {
        return MAIN_THREAD;
    }

    /**
     * 获取工作线程
     *
     * @return 工作线程
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/8 10:12 AM
     */
    public ThreadPoolExecutor getWorkExecutorService() {
        return WORK_EXECUTOR_SERVICE;
    }

    /**
     * 获取延时线程
     *
     * @return 延时线程
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/8 10:13 AM
     */
    public ScheduledExecutorService getScheduledExecutorService() {
        return SCHEDULED_EXECUTOR_SERVICE;
    }

    /**
     * 获取即时线程
     *
     * @return 即时线程
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/8 10:13 AM
     */
    public Executor getImmediateExecutor() {
        return IMMEDIATE_EXECUTOR;
    }

    private static class ImmediateExecutor implements Executor {

        private static final int MAX_DEPTH = 15;
        private final ThreadLocal<Integer> executionDepth = new ThreadLocal<>();

        /**
         * 增加深度
         *
         * @return 返回线程调用栈深度值
         */
        private int incrementDepth() {
            Integer oldDepth = executionDepth.get();
            if (oldDepth == null) {
                oldDepth = 0;
            }
            int newDepth = oldDepth + 1;
            executionDepth.set(newDepth);
            return newDepth;
        }

        /**
         * 减少深度
         *
         * @return 返回线程调用栈深度值
         */
        @SuppressWarnings("UnusedReturnValue")
        private int decrementDepth() {
            Integer oldDepth = executionDepth.get();
            if (oldDepth == null) {
                oldDepth = 0;
            }
            int newDepth = oldDepth - 1;
            if (newDepth == 0) {
                executionDepth.remove();
            } else {
                executionDepth.set(newDepth);
            }
            return newDepth;
        }

        /**
         * 核心区域切换执行线程
         *
         * @param runnable : 任务体
         */
        @Override
        public void execute(@NonNull Runnable runnable) {
            int depth = incrementDepth();
            try {
                if (depth <= MAX_DEPTH) {
                    runnable.run();
                } else {
                    ExecutorCore.INSTANCE.getWorkExecutorService().execute(runnable);
                }
            } finally {
                decrementDepth();
            }
        }
    }
}
