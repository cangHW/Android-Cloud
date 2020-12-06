package com.proxy.service.utils.monitor;

import android.view.View;

import com.proxy.service.api.monitor.ViewMonitorHelper;
import com.proxy.service.api.monitor.visible.VisibleMonitorBuilder;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.monitor.visible.VisibleMonitorBuilderImpl;

/**
 * @author : cangHX
 * on 2020/11/11  9:30 PM
 */
public class ViewMonitorHelperImpl implements ViewMonitorHelper {

    private View mView;

    public ViewMonitorHelperImpl(View view) {
        this.mView = view;
        if (view == null) {
            Logger.Error("View is null so this monitoring is invalid");
        }
    }

    /**
     * 创建显示状态监控
     *
     * @return 显示状态监控构造器
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:16 PM
     */
    @Override
    public VisibleMonitorBuilder createVisibleMonitor() {
        return new VisibleMonitorBuilderImpl(mView);
    }
}
