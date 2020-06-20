package com.proxy.service.api.services;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.proxy.service.base.BaseService;

/**
 * @author: cangHX
 * on 2020/06/10  19:00
 * <p>
 * app相关
 */
public interface CloudUtilsAppService extends BaseService {

    /**
     * 获取当前app的目标设备SDK版本
     *
     * @return uid
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-10 19:04
     */
    int getTargetSdkVersion();

    /**
     * 获取当前app的uid
     *
     * @return uid
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-10 19:04
     */
    String getUid();

    /**
     * 获取当前app包名
     *
     * @return 包名
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-10 19:04
     */
    String getPackageName();

    /**
     * 获取当前app版本code
     * @see #getLongVersionCode
     *
     * @return 版本code
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 09:52
     */
    @Deprecated
    int getVersionCode();

    /**
     * 获取当前app版本code
     *
     * @return 版本code
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 09:52
     */
    @RequiresApi(Build.VERSION_CODES.P)
    long getLongVersionCode();

    /**
     * 获取当前app版本name
     *
     * @return 版本name
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 09:53
     */
    String getVersionName();

    /**
     * 判断app是否处于后台
     *
     * @return true 后台，false 前台
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:18
     */
    boolean isBackground();
}
