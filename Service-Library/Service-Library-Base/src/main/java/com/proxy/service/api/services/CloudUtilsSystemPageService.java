package com.proxy.service.api.services;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.proxy.service.base.BaseService;

/**
 * 系统页面相关
 *
 * @author: cangHX
 * on 2020/06/18  13:47
 */
public interface CloudUtilsSystemPageService extends BaseService {

    /**
     * 打开应用设置页面
     *
     * @param packageName : 包名，为空默认使用当前app的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:11
     */
    void openAppSetting(@Nullable String packageName);

    /**
     * 打开应用通知设置页面
     *
     * @param packageName : 包名，为空默认使用当前app的数据
     * @param uid         : 应用的uid，为空默认使用当前app的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:11
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    void openNotificationSetting(@Nullable String packageName, @Nullable String uid);

    /**
     * 打电话
     *
     * @param phoneNumber : 准备拨打的电话号码
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-18 14:26
     */
    void openCall(@Nullable String phoneNumber);
}
