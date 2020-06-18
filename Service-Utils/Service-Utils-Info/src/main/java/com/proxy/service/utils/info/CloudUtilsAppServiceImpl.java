package com.proxy.service.utils.info;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.cloud.annotations.CloudService;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsAppService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.cache.Cache;

import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/11  12:10
 */
@CloudService(serviceTag = CloudServiceTagUtils.UTILS_APP)
public class CloudUtilsAppServiceImpl implements CloudUtilsAppService {

    /**
     * 获取当前app的目标设备SDK版本
     *
     * @return uid
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-10 19:04
     */
    @Override
    public int getTargetSdkVersion() {
        int targetSdkVersion = -1;
        Context context = ContextManager.getCurrentActivity();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return targetSdkVersion;
        }
        try {
            PackageManager packageManager = Cache.getPackageManager(context);
            if (packageManager == null) {
                return targetSdkVersion;
            }
            PackageInfo info = packageManager.getPackageInfo(getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (Throwable e) {
            Logger.Debug(CloudApiError.PACKAGE_MANAGER_ERROR.setMsg(e).build());
        }
        return targetSdkVersion;
    }

    /**
     * 获取当前app的uid
     *
     * @return uid
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-10 19:04
     */
    @Override
    public String getUid() {
        Context context = ContextManager.getCurrentActivity();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return "";
        }
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        return String.valueOf(applicationInfo.uid);
    }

    /**
     * 获取当前app包名
     *
     * @return 包名
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-10 19:04
     */
    @Override
    public String getPackageName() {
        Context context = ContextManager.getCurrentActivity();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return "";
        }
        return context.getPackageName();
    }

    /**
     * 获取当前app版本code
     *
     * @return 版本code
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 09:52
     */
    @Override
    public int getVersionCode() {
        int versionCode = -1;
        Context context = ContextManager.getCurrentActivity();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return versionCode;
        }
        try {
            PackageManager packageManager = Cache.getPackageManager(context);
            if (packageManager == null) {
                return versionCode;
            }
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (Throwable e) {
            Logger.Debug(CloudApiError.PACKAGE_MANAGER_ERROR.setMsg(e).build());
        }
        return versionCode;
    }

    /**
     * 获取当前app版本code
     *
     * @return 版本code
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 09:52
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public long getLongVersionCode() {
        long versionCode = -1;
        Context context = ContextManager.getCurrentActivity();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return versionCode;
        }
        try {
            PackageManager packageManager = Cache.getPackageManager(context);
            if (packageManager == null) {
                return versionCode;
            }
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.getLongVersionCode();
        } catch (Throwable e) {
            Logger.Debug(CloudApiError.PACKAGE_MANAGER_ERROR.setMsg(e).build());
        }
        return versionCode;
    }

    /**
     * 获取当前app版本name
     *
     * @return 版本name
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 09:53
     */
    @Override
    public String getVersionName() {
        String versionName = "";
        Context context = ContextManager.getCurrentActivity();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return "";
        }
        try {
            PackageManager packageManager = Cache.getPackageManager(context);
            if (packageManager == null) {
                return versionName;
            }
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (Throwable e) {
            Logger.Debug(CloudApiError.PACKAGE_MANAGER_ERROR.setMsg(e).build());
        }
        return versionName;
    }

    /**
     * 判断app是否处于后台
     *
     * @return true 后台，false 前台
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:18
     */
    @Override
    public boolean isBackground() {
        boolean isInBackground = ContextManager.getShowCount() == 0;

        Context context = ContextManager.getCurrentActivity();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return isInBackground;
        }

        ActivityManager am = Cache.getActivityManager(context);

        if (am == null) {
            return isInBackground;
        }
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    //前台程序
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            if (activeProcess.equals(context.getPackageName())) {
                                isInBackground = false;
                            }
                        }
                    }
                }
            } else {
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                if (taskInfo == null || taskInfo.size() <= 0) {
                    return isInBackground;
                }
                @SuppressLint({"NewApi", "LocalSuppress"}) ComponentName componentInfo = taskInfo.get(0).topActivity;
                if (componentInfo == null) {
                    return isInBackground;
                }
                if (componentInfo.getPackageName().equals(context.getPackageName())) {
                    isInBackground = false;
                }
            }
        } catch (Throwable e) {
            Logger.Debug(CloudApiError.ACTIVITY_MANAGER_ERROR.setMsg(e).build());
            return isInBackground;
        }
        return isInBackground;
    }
}
