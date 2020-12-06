package com.proxy.service.utils.info;

import android.view.View;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.monitor.ViewMonitorHelper;
import com.proxy.service.api.services.CloudUtilsViewMonitorService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.utils.monitor.ViewMonitorHelperImpl;

/**
 * @author : cangHX
 * on 2020/11/11  9:29 PM
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_VIEW_MONITOR)
public class UtilsViewMonitorServiceImpl implements CloudUtilsViewMonitorService {
    /**
     * 设置准备监控的view
     *
     * @param view : 准备监控的view
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:53 PM
     */
    @Override
    public ViewMonitorHelper with(@NonNull View view) {
        return new ViewMonitorHelperImpl(view);
    }
}
