package com.proxy.service.utils.monitor.visible;

import com.proxy.service.api.monitor.visible.CloudVisibleMonitorCallback;
import com.proxy.service.api.log.Logger;

/**
 * @author : cangHX
 * on 2020/11/11  9:34 PM
 */
public class VisibleMonitorCallbackImpl implements CloudVisibleMonitorCallback {
    /**
     * view 显示
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:15 PM
     */
    @Override
    public void onShow() {
        Logger.Info("展示");
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
        Logger.Info("隐藏");
    }
}
