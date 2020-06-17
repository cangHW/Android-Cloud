package com.proxy.service.api.services;

import androidx.annotation.NonNull;

import com.proxy.service.base.BaseService;

/**
 * @author: cangHX
 * on 2020/06/11  10:30
 * <p>
 * 系统相关
 */
public interface CloudUtilsSystemService extends BaseService {

    /**
     * 获取ID回调
     */
    interface AppIdsUpdater {
        /**
         * 回调获取到的数据
         *
         * @param id : 获取到的ID数据，有可能为空字串
         * @throws Throwable : 可能会出现的异常
         * @version: 1.0
         * @author: cangHX
         * @date: 2020-06-11 10:51
         */
        void OnIdsAvalId(@NonNull String id) throws Throwable;
    }

    /**
     * 获取设备ID
     *
     * @return 设备ID
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:31
     */
    String getDeviceId();

    /**
     * GPS定位是否开启
     *
     * @return true 开启，false 未开启，默认未开启
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:32
     */
    boolean isGpsEnabled();

    /**
     * MSA，获取aaid
     *
     * @param appIdsUpdater : 回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:30
     */
    void getAAID(@NonNull AppIdsUpdater appIdsUpdater);

    /**
     * MSA，获取oaid
     *
     * @param appIdsUpdater : 回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:30
     */
    void getOAID(@NonNull AppIdsUpdater appIdsUpdater);

    /**
     * MSA，获取vaid
     *
     * @param appIdsUpdater : 回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:30
     */
    void getVAID(@NonNull AppIdsUpdater appIdsUpdater);

}
