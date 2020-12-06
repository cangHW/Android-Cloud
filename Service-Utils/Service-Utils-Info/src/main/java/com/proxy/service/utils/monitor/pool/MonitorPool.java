package com.proxy.service.utils.monitor.pool;

import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.NonNull;

/**
 * @author : cangHX
 * on 2020/11/11  9:46 PM
 */
public class MonitorPool {

    private static final HandlerThread THREAD = new HandlerThread("View-Monitor");

    static {
        THREAD.start();
    }

    private final Handler mHandler;

    public MonitorPool() {
        mHandler = new Handler(THREAD.getLooper());
    }

    public void post(@NonNull Runnable r) {
        mHandler.post(r);
    }

    public void post(@NonNull Runnable r, long delayMillis) {
        mHandler.postDelayed(r, delayMillis);
    }

    public void remove(@NonNull Runnable r) {
        mHandler.removeCallbacks(r);
    }

}
