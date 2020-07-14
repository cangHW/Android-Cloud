package com.proxy.service.utils.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * @author: cangHX
 * on 2020/06/24  18:11
 */
public class HandleUtils {

    private static final HandlerThread HANDLER_THREAD = new HandlerThread("cloud_thread_handler");

    static {
        HANDLER_THREAD.start();
    }

    private static final Handler HANDLER_BG = new Handler(HANDLER_THREAD.getLooper());

    private static final Handler HANDLER_MAIN = new Handler(Looper.getMainLooper());

    /**
     * 主线程执行
     *
     * @param runnable : 执行体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-24 18:14
     */
    public static void postMain(Runnable runnable) {
        HANDLER_MAIN.post(runnable);
    }

    /**
     * 子线程执行
     *
     * @param runnable : 执行体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-24 18:14
     */
    public static void postBg(Runnable runnable) {
        HANDLER_BG.post(runnable);
    }
}
