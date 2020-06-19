package com.proxy.service.utils.oaid.empty;

import com.proxy.service.api.services.CloudUtilsSystemInfoService;
import com.proxy.service.utils.oaid.callback.OaidRequestCallback;

/**
 * @author: cangHX
 * on 2020/06/19  18:31
 * <p>
 * 默认实现类
 */
public class OaidRequestCallbackEmpty implements OaidRequestCallback {
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
    public void request(CloudUtilsSystemInfoService.AppIdsUpdater appIdsUpdater) {
        appIdsUpdater.onIdsAvalId("");
    }
}
