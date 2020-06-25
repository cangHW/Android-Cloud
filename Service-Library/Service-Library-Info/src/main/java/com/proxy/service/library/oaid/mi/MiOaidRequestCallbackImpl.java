package com.proxy.service.library.oaid.mi;

import androidx.annotation.NonNull;

import com.proxy.service.api.services.CloudUtilsSystemInfoService;
import com.proxy.service.library.oaid.callback.OaidRequestCallback;

/**
 * @author: cangHX
 * on 2020/06/20  13:23
 */
public class MiOaidRequestCallbackImpl implements OaidRequestCallback {
    /**
     * 功能是否兼容当前设备
     *
     * @return true 兼容，false 不兼容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 18:01
     */
    @Override
    public boolean isSupported() {
        return false;
    }

    /**
     * 请求oaid
     *
     * @param appIdsUpdater : 回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 18:17
     */
    @Override
    public void request(@NonNull CloudUtilsSystemInfoService.AppIdsUpdater appIdsUpdater) {
        appIdsUpdater.onIdsAvalId("");
    }
}
