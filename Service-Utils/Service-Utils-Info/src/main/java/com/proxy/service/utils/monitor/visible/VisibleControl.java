package com.proxy.service.utils.monitor.visible;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import com.proxy.service.api.monitor.visible.CloudVisibleMonitorCallback;
import com.proxy.service.utils.monitor.pool.MonitorPool;

import java.lang.ref.WeakReference;

/**
 * @author : cangHX
 * on 2020/11/11  10:16 PM
 */
public class VisibleControl {

    /**
     * 循环池子
     */
    private final MonitorPool mMonitorPool = new MonitorPool();
    /**
     * 循环任务
     */
    private final Runnable mRunnable;
    /**
     * 标记 view 是否显示，默认为 false
     */
    private boolean mVisibility;
    /**
     * 准备监控的 view
     */
    private View mView;
    /**
     * 监控回调
     */
    private CloudVisibleMonitorCallback mCallback;
    /**
     * 有效区域比例，0 — 1，百分比
     */
    private final float mArea;
    /**
     * 轮询间隔时间
     */
    private final long mDelayMillis;

    public VisibleControl(View view, float area, long delayMillis, CloudVisibleMonitorCallback callback) {
        this.mVisibility = false;
        this.mView = view;
        this.mDelayMillis = delayMillis;
        this.mCallback = callback;
        this.mArea = area;
        this.mRunnable = new HandlerRunnable(this);
    }

    /**
     * 开始
     */
    public void start() {
        mMonitorPool.remove(mRunnable);
        mMonitorPool.post(mRunnable);
    }

    /**
     * 重置
     */
    public void reset() {
        mMonitorPool.remove(mRunnable);
        mVisibility = false;
    }

    /**
     * 暂停
     */
    public void stop() {
        mMonitorPool.remove(mRunnable);
        mMonitorPool.post(new Runnable() {
            @Override
            public void run() {
                checkCanCallback(false, mCallback);
            }
        });
    }

    /**
     * 销毁
     */
    public void destroy() {
        mMonitorPool.remove(mRunnable);
        mMonitorPool.post(new Runnable() {
            @Override
            public void run() {
                mView = null;
                mCallback = null;
            }
        });
    }

    /**
     * 辅助循环，防止内存泄漏
     */
    private static class HandlerRunnable implements Runnable {

        private final WeakReference<VisibleControl> mReference;

        private HandlerRunnable(VisibleControl monitor) {
            mReference = new WeakReference<>(monitor);
        }

        @Override
        public void run() {
            final VisibleControl monitor = mReference.get();
            if (monitor == null) {
                return;
            }
            try {
                monitor.checkVisibility();
            } catch (Throwable ignored) {
            }
            try {
                monitor.mMonitorPool.post(monitor.mRunnable, monitor.mDelayMillis);
            } catch (Throwable ignored) {
            }
        }
    }

    /**
     * 检测 view 状态
     */
    private void checkVisibility() {
        if (mView == null) {
            return;
        }
        boolean window = checkWindow(mView);
        if (!window) {
            checkCanCallback(false, mCallback);
            return;
        }
        boolean normal = checkNormal(mView);
        if (!normal) {
            checkCanCallback(false, mCallback);
            return;
        }
        boolean location = checkLocation(mView);
        checkCanCallback(location, mCallback);
    }

    /**
     * 检测当前是否允许回调 view 状态
     */
    private void checkCanCallback(boolean visibility, CloudVisibleMonitorCallback callback) {
        if (callback == null) {
            return;
        }
        if (visibility == mVisibility) {
            return;
        }
        if (visibility) {
            callback.onShow();
        } else {
            callback.onGone();
        }
        mVisibility = visibility;
    }

    /**
     * 检查显示隐藏状态
     */
    private boolean checkNormal(View view) {
        if (view.getVisibility() != View.VISIBLE) {
            return false;
        }
        View currentView = view;
        while (currentView.getParent() instanceof ViewGroup) {
            ViewGroup currentParent = (ViewGroup) currentView.getParent();
            if (currentParent.getVisibility() != View.VISIBLE) {
                return false;
            }
            currentView = currentParent;
        }
        return true;
    }

    /**
     * 检查可见区域
     */
    private boolean checkLocation(View view) {
        Rect rect = new Rect();
        boolean flag = view.getLocalVisibleRect(rect);
        if (!flag) {
            return false;
        }
        return rect.width() * rect.height() >= view.getMeasuredWidth() * view.getMeasuredHeight() * mArea;
    }

    /**
     * 检查窗口可见状态
     */
    private boolean checkWindow(View view) {
        return view.getWindowVisibility() == View.VISIBLE;
    }
}
