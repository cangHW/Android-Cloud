package com.proxy.service.utils.info;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsAppService;
import com.proxy.service.api.services.CloudUtilsBitmapService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.log.Logger;
import com.proxy.service.utils.cache.Cache;

import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/11  12:10
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_APP)
public class UtilsAppServiceImpl implements CloudUtilsAppService {

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
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
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
            Logger.Error(CloudApiError.PACKAGE_MANAGER_ERROR.setMsg(e).build(), e);
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
    @NonNull
    @Override
    public String getUid() {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
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
    @NonNull
    @Override
    public String getPackageName() {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return "";
        }
        return context.getPackageName();
    }

    /**
     * 获取当前app图标
     *
     * @return 图标
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-10 19:04
     */
    @Override
    public Bitmap getIcon() {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return null;
        }
        Bitmap bitmap = null;
        try {
            Drawable drawable = context.getApplicationInfo().loadIcon(Cache.getPackageManager(context));
            CloudUtilsBitmapService bitmapService = CloudSystem.getService(CloudServiceTagUtils.UTILS_BITMAP);
            if (bitmapService != null) {
                bitmap = bitmapService.toBitmap(drawable, Bitmap.Config.ARGB_8888);
            }
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        return bitmap;
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
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
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
            Logger.Error(CloudApiError.PACKAGE_MANAGER_ERROR.setMsg(e).build(), e);
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
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
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
            Logger.Error(CloudApiError.PACKAGE_MANAGER_ERROR.setMsg(e).build(), e);
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
    @NonNull
    @Override
    public String getVersionName() {
        String versionName = "";
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
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
            Logger.Error(CloudApiError.PACKAGE_MANAGER_ERROR.setMsg(e).build(), e);
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

        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return isInBackground;
        }

        ActivityManager am = Cache.getActivityManager(context);

        if (am == null) {
            return isInBackground;
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            try {
                List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    //前台程序
                    if (processInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        continue;
                    }
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                            break;
                        }
                    }
                    break;
                }
            } catch (Throwable throwable) {
                Logger.Error(CloudApiError.ACTIVITY_MANAGER_ERROR.setMsg(throwable).build(), throwable);
            }
            return isInBackground;
        }

        try {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            if (taskInfo == null || taskInfo.size() <= 0) {
                return isInBackground;
            }
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo == null) {
                return isInBackground;
            }
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        } catch (Throwable throwable) {
            Logger.Error(CloudApiError.ACTIVITY_MANAGER_ERROR.setMsg(throwable).build(), throwable);
        }
        return isInBackground;
    }
}
