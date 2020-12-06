package com.proxy.service.api.services;

import android.view.View;

import androidx.annotation.NonNull;

import com.proxy.service.api.monitor.ViewMonitorHelper;
import com.proxy.service.base.BaseService;

/**
 * view 监控相关
 *
 * @author : cangHX
 * on 2020/11/11  9:05 PM
 */
public interface CloudUtilsViewMonitorService extends BaseService {

    /**
     * 设置准备监控的view
     *
     * @param view : 准备监控的view
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:53 PM
     */
    ViewMonitorHelper with(@NonNull View view);
}
