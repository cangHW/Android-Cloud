package com.proxy.service.api.services;

import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.WorkerThread;

import com.proxy.service.base.BaseService;

import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/11  09:54
 * <p>
 * 安装相关
 */
public interface CloudUtilsInstallService extends BaseService {

    /**
     * 已安装app的信息
     */
    class AppInfo {
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

    /**
     * 对应包名的app是否安装
     *
     * @param packageName : 包名
     * @return 是否安装，true 已安装，false 未安装
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-10 19:02
     */
    boolean isInstallApp(@NonNull String packageName);

    /**
     * 安装应用
     *
     * @param apkPath : 安装包路径
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 09:55
     */
    void installApp(@NonNull String apkPath);

    /**
     * 获取对应apk的包名
     *
     * @param apkPath : 安装包路径
     * @return apk的包名
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 15:11
     */
    String getPackageName(@NonNull String apkPath);

    /**
     * 卸载应用
     *
     * @param packageName : 包名
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 09:56
     */
    void unInstallApp(@NonNull String packageName);

    /**
     * 获取所有已安装应用
     *
     * @return 获取到的已安装应用
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:06
     */
    @NonNull
    @WorkerThread
    List<AppInfo> getAllInstallAppsInfo();

    /**
     * 打开应用设置页面
     *
     * @param packageName : 包名
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:11
     */
    void openAppSetting(@NonNull String packageName);
}
