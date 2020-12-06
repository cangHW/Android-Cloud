package com.proxy.service.utils.monitor.visible;

import android.view.View;

import com.proxy.service.api.monitor.visible.CloudVisibleMonitorCallback;
import com.proxy.service.api.monitor.visible.VisibleMonitorBuilder;
import com.proxy.service.api.monitor.visible.VisibleMonitorHelper;

/**
 * @author : cangHX
 * on 2020/11/11  9:33 PM
 */
public class VisibleMonitorBuilderImpl implements VisibleMonitorBuilder {

    private final View mView;
    private float mArea = 0.5f;
    private long mDuration = 1000;
    private long mDelayMillis = 500;

    private CloudVisibleMonitorCallback mCallback = new VisibleMonitorCallbackImpl();

    public VisibleMonitorBuilderImpl(View view) {
        this.mView = view;
    }

    /**
     * 设置曝光的有效区域比例（0—1 百分比）,展示多少算一次有效曝光，默认为 0.5
     *
     * @param area : 有效区域比例（0—1 百分比）
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    @Override
    public VisibleMonitorBuilder setArea(float area) {
        if (area > 0 && area <= 1) {
            mArea = area;
        }
        return this;
    }

    /**
     * 设置曝光的有效时长（单位：毫秒）,多久算一次有效曝光, 默认为 1000
     *
     * @param duration : 曝光的有效时长
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    @Override
    public VisibleMonitorBuilder setDuration(long duration) {
        if (duration >= 0) {
            mDuration = duration;
        }
        return this;
    }

    /**
     * 设置曝光的检测间隔时间（单位：毫秒），时间越短, 灵敏度越高，默认为 500
     *
     * @param delayMillis : 曝光的检测间隔时间
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    @Override
    public VisibleMonitorBuilder setDelayMillis(long delayMillis) {
        if (delayMillis >= 0) {
            mDelayMillis = delayMillis;
        }
        return this;
    }

    /**
     * 设置监控的回调接口
     *
     * @param callback : 监控的回调接口
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    @Override
    public VisibleMonitorBuilder setCallback(CloudVisibleMonitorCallback callback) {
        if (callback != null) {
            mCallback = callback;
        }
        return this;
    }

    /**
     * 创建监控
     *
     * @return 监控辅助器
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    @Override
    public VisibleMonitorHelper build() {
        VisibleMonitorHelperImpl helper = new VisibleMonitorHelperImpl();

        VisibleControl control = new VisibleControl(mView, mArea, mDelayMillis, helper);
        helper.setDuration(mDuration);
        helper.setVisibleControl(control);
        helper.setVisibleMonitorCallback(mCallback);

        return helper;
    }
}
