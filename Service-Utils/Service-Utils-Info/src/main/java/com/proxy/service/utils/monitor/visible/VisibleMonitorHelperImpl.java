package com.proxy.service.utils.monitor.visible;

import android.os.Handler;

import com.proxy.service.api.monitor.visible.CloudVisibleMonitorCallback;
import com.proxy.service.api.monitor.visible.VisibleMonitorHelper;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : cangHX
 * on 2020/11/11  9:41 PM
 */
public class VisibleMonitorHelperImpl implements VisibleMonitorHelper, CloudVisibleMonitorCallback {

    private final Handler mTimeHandler = new Handler();
    private final Runnable mTimerRunnable;
    private final AtomicBoolean isShow = new AtomicBoolean(false);

    private VisibleControl mVisibleControl;
    private long mDuration;
    private CloudVisibleMonitorCallback mCallback;

    public VisibleMonitorHelperImpl() {
        mTimerRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isShow.get() && mCallback != null) {
                    mCallback.onShow();
                }
                isShow.set(true);
            }
        };
    }

    public void setVisibleControl(VisibleControl visibleControl) {
        this.mVisibleControl = visibleControl;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public void setVisibleMonitorCallback(CloudVisibleMonitorCallback callback) {
        this.mCallback = callback;
    }

    /**
     * 开始
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    @Override
    public void start() {
        if (mVisibleControl != null) {
            mVisibleControl.start();
        }
    }

    /**
     * 还原数据，重置为原始状态
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    @Override
    public void reset() {
        if (mVisibleControl != null) {
            mVisibleControl.reset();
        }
    }

    /**
     * 暂停
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    @Override
    public void stop() {
        if (mVisibleControl != null) {
            mVisibleControl.stop();
        }
    }

    /**
     * 销毁
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    @Override
    public void destroy() {
        if (mVisibleControl != null) {
            mVisibleControl.destroy();
        }
    }

    /**
     * view 显示
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:15 PM
     */
    @Override
    public void onShow() {
        if (mTimerRunnable != null) {
            //安全处理，每次开始前，都把可能存在的runnable移除
            mTimeHandler.removeCallbacks(mTimerRunnable);
            mTimeHandler.postDelayed(mTimerRunnable, mDuration);
        }
    }

    /**
     * view 隐藏
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:15 PM
     */
    @Override
    public void onGone() {
        if (mTimerRunnable != null) {
            mTimeHandler.removeCallbacks(mTimerRunnable);
            if (isShow.get() && mCallback != null) {
                mCallback.onGone();
            }
            isShow.set(false);
        }
    }
}
