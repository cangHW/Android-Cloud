package com.proxy.service.api.monitor;

import com.proxy.service.api.monitor.visible.VisibleMonitorBuilder;

/**
 * @author : cangHX
 * on 2020/11/11  9:10 PM
 */
public interface ViewMonitorHelper {

    /**
     * 创建显示状态监控
     *
     * @return 显示状态监控构造器
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:16 PM
     */
    VisibleMonitorBuilder createVisibleMonitor();

}
