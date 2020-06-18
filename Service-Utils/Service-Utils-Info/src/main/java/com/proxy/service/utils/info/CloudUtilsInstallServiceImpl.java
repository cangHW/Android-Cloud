package com.proxy.service.utils.info;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;

import com.cloud.annotations.CloudService;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsAppService;
import com.proxy.service.api.services.CloudUtilsInstallService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.cache.Cache;
import com.proxy.service.utils.provider.CloudProvider;
import com.proxy.service.utils.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/11  12:41
 */
@CloudService(serviceTag = CloudServiceTagUtils.UTILS_INSTALL)
public class CloudUtilsInstallServiceImpl implements CloudUtilsInstallService {

    /**
     * 对应包名的app是否安装
     *
     * @param packageName : 包名
     * @return 是否安装，true 已安装，false 未安装
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-10 19:02
     */
    @Override
    public boolean isInstallApp(@NonNull String packageName) {
        Context context = ContextManager.getCurrentActivity();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return false;
        }
        PackageManager packageManager = Cache.getPackageManager(context);
        if (packageManager == null) {
            return false;
        }
        try {
            return packageManager.getLaunchIntentForPackage(packageName) != null;
        } catch (Throwable ignored) {
        }
        return false;
    }

    /**
     * 安装应用
     *
     * @param apkPath : 安装包路径
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 09:55
     */
    @SuppressLint("WrongConstant")
    @Override
    public void installApp(@NonNull String apkPath) {
        File file = new File(apkPath);
        if (!file.exists()) {
            Logger.Error(CloudApiError.DATA_EMPTY.setMsg("The apk file is empty. " + apkPath).build());
            return;
        }
        Context context = ContextManager.getCurrentActivity();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type = "application/vnd.android.package-archive";

        Uri uri;
        CloudUtilsAppService service = new CloudUtilsAppServiceImpl();
        boolean isSdkVersionReady = Build.VERSION.SDK_INT < Build.VERSION_CODES.N;
        // 部分机型在系统版本为7.0，但使用provier形式会崩溃。所以判断如果targetV如果<=23并且系统版本为7.0时仍然使用file://形式
        boolean isTargetReady = Build.VERSION.SDK_INT == Build.VERSION_CODES.N && service.getTargetSdkVersion() <= Build.VERSION_CODES.M;
        if (isSdkVersionReady || isTargetReady) {
            uri = Uri.fromFile(file);
        } else {
            String provider = Utils.getProviderAuthoritiesFromManifest(CloudProvider.class.getName(), "proxy_service_provider");
            uri = Uri.parse("content://" + provider + apkPath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, type);
        PackageManager packageManager = Cache.getPackageManager(context);
        if (packageManager == null) {
            return;
        }
        try {
            if (packageManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES).size() <= 0) {
                Logger.Debug("install failed");
                return;
            }
            context.startActivity(intent);
        } catch (Throwable throwable) {
            Logger.Debug(throwable.getMessage());
        }
    }

    /**
     * 获取对应apk的包名
     *
     * @param apkPath : 安装包路径
     * @return apk的包名
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 15:11
     */
    @Override
    public String getPackageName(@NonNull String apkPath) {
        Context context = ContextManager.getCurrentActivity();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return "";
        }
        PackageManager packageManager = Cache.getPackageManager(context);
        if (packageManager == null) {
            return "";
        }
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        } catch (Throwable throwable) {
            Logger.Debug(throwable.getMessage());
        }
        if (packageInfo == null) {
            return "";
        }
        ApplicationInfo appInfo = packageInfo.applicationInfo;
        return appInfo.packageName;
    }

    /**
     * 卸载应用
     *
     * @param packageName : 包名
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 09:56
     */
    @Override
    public void unInstallApp(@NonNull String packageName) {
        Context context = ContextManager.getCurrentActivity();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }

    /**
     * 获取所有已安装应用
     *
     * @return 获取到的已安装应用
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:06
     */
    @SuppressLint("NewApi")
    @NonNull
    @Override
    public List<AppInfo> getAllInstallAppsInfo() {
        List<AppInfo> infoList = new ArrayList<>();
        Context context = ContextManager.getCurrentActivity();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return infoList;
        }
        PackageManager packageManager = Cache.getPackageManager(context);
        if (packageManager == null) {
            return infoList;
        }

        List<PackageInfo> apps = packageManager.getInstalledPackages(0);

        for (PackageInfo packageInfo : apps) {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;

            AppInfo appInfo = new AppInfo();
            appInfo.icon = applicationInfo.loadIcon(packageManager);
            appInfo.name = applicationInfo.loadLabel(packageManager).toString();
            appInfo.packageName = packageInfo.packageName;
            appInfo.versionCode = packageInfo.versionCode;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appInfo.longVersionCode = packageInfo.getLongVersionCode();
            } else {
                appInfo.longVersionCode = packageInfo.versionCode;
            }
            appInfo.versionName = packageInfo.versionName;
            appInfo.isInstallSd = (applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0;

            boolean isUpDatedSystem = (applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
            boolean isSystem = (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM;

            appInfo.isSystemApp = isUpDatedSystem || isSystem;

            infoList.add(appInfo);
        }

        return infoList;
    }

}
