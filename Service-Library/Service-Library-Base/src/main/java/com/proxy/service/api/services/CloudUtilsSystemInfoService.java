package com.proxy.service.api.services;

import androidx.annotation.NonNull;

import com.proxy.service.api.annotations.BRAND;
import com.proxy.service.base.BaseService;

/**
 * @author: cangHX
 * on 2020/06/11  10:30
 * <p>
 * 系统信息相关
 */
public interface CloudUtilsSystemInfoService extends BaseService {

    /**
     * 获取ID回调
     */
    interface AppIdsUpdater {
        /**
         * 回调获取到的数据
         *
         * @param id : 获取到的ID数据，有可能为空字串
         * @version: 1.0
         * @author: cangHX
         * @date: 2020-06-11 10:51
         */
        void onIdsAvalId(@NonNull String id);
    }

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
    String getImel();

    /**
     * 获取当前设备厂商
     *
     * @return 返回当前设备厂商信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 18:36
     */
    @NonNull
    @BRAND
    String getBrand();

    /**
     * 获取当前设备型号
     *
     * @return 返回当前设备型号
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 18:37
     */
    String getModel();

    /**
     * MSA，获取oaid
     *
     * @param appIdsUpdater : 回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:30
     */
    void getOaid(@NonNull AppIdsUpdater appIdsUpdater);

}
