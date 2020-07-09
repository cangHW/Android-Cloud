package com.proxy.service.api.info;

import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * 已安装app的信息
 *
 * @author: cangHX
 * on 2020/06/24  17:14
 */
public class CloudAppInfo {

    /**
     * app名字
     */
    public String name;
    /**
     * app图标
     */
    public Drawable icon;
    /**
     * app包名
     */
    public String packageName;
    /**
     * app版本name
     */
    public String versionName;
    /**
     * app版本code
     *
     * @see #longVersionCode
     */
    @Deprecated
    public int versionCode;
    /**
     * app版本code
     */
    @RequiresApi(Build.VERSION_CODES.P)
    public long longVersionCode;
    /**
     * 是否安装在sd卡
     */
    public boolean isInstallSd;
    /**
     * 是否是普通应用(非系统应用)
     */
    public boolean isSystemApp;

}
