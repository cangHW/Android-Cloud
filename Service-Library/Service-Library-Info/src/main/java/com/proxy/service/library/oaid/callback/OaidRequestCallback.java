package com.proxy.service.library.oaid.callback;

import androidx.annotation.NonNull;

import com.proxy.service.api.services.CloudUtilsSystemInfoService;

/**
 * @author: cangHX
 * on 2020/06/19  18:00
 */
public interface OaidRequestCallback {

    /**
     * 功能是否兼容当前设备
     *
     * @return true 兼容，false 不兼容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 18:01
     */
    boolean isSupported();

    /**
     * 请求oaid
     *
     * @param appIdsUpdater : 回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 18:17
     */
    void request(@NonNull CloudUtilsSystemInfoService.AppIdsUpdater appIdsUpdater);

}
