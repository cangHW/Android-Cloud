package com.proxy.service.ui.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * @author: cangHX
 * on 2020/07/08  18:46
 */
public class TaskUtils {

    private static Handler mMainHandler = new Handler(Looper.getMainLooper());
    private static HandlerThread mThread = new HandlerThread("background");

    static {
        mThread.start();
    }

    private static Handler mThreadHandler = new Handler(mThread.getLooper());

    /**
     * 是否在主线程
     *
     * @return true 是，false 不是
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-08 20:28
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 执行ui线程任务
     *
     * @param runnable : 任务体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-08 20:28
     */
    public static void postUi(Runnable runnable) {
        mMainHandler.post(runnable);
    }

    /**
     * 执行子线程任务
     *
     * @param runnable : 任务体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-08 20:29
     */
    public static void postBg(Runnable runnable) {
        mThreadHandler.post(runnable);
    }

}
