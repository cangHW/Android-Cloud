package com.proxy.service.library.info;

import android.os.Build;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudService;
import com.proxy.service.api.services.CloudUtilsSystemInfoService;
import com.proxy.service.api.tag.CloudServiceTagLibrary;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.library.oaid.OaidManager;
import com.proxy.service.library.oaid.callback.OaidRequestCallback;

/**
 * @author: cangHX
 * on 2020/06/11  12:45
 */
@CloudService(serviceTag = CloudServiceTagLibrary.UTILS_SYSTEM_INFO)
public class UtilsSystemInfoServiceImpl implements CloudUtilsSystemInfoService {

    /**
     * 获取设备ID
     * DeviceId
     *
     * @return 设备ID
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:31
     */
    @NonNull
    @Override
    public String getImel() {
        //TODO imel相关
        return "";
    }

    /**
     * 获取当前设备
     *
     * @return 返回当前设备信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 18:36
     */
    @NonNull
    @Override
    public String getBrand() {
        return Build.BRAND;
    }

    /**
     * 获取当前设备型号
     *
     * @return 返回当前设备型号
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 18:37
     */
    @Override
    public String getModel() {
        return Build.MODEL;
    }

    /**
     * MSA，获取oaid
     *
     * @param appIdsUpdater : 回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:30
     */
    @Override
    public void getOaid(@NonNull AppIdsUpdater appIdsUpdater) {
        OaidRequestCallback oaidRequestCallback = OaidManager.getOaidRequestCallback();
        if (!oaidRequestCallback.isSupported()) {
            Logger.Info("The current device does not support obtaining OAID");
            return;
        }
        oaidRequestCallback.request(appIdsUpdater);
    }

}
