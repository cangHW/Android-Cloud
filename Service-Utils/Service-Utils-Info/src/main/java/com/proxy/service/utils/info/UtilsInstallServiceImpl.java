package com.proxy.service.utils.info;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.install.CloudInstallCallback;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.install.CloudInstallStatusEnum;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.install.CloudAppInfo;
import com.proxy.service.api.services.CloudUtilsAppService;
import com.proxy.service.api.services.CloudUtilsInstallService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.cache.Cache;
import com.proxy.service.utils.install.InstallReceiverListenerManager;
import com.proxy.service.utils.provider.UtilsProvider;
import com.proxy.service.utils.receiver.UtilsBroadcastReceiver;
import com.proxy.service.utils.util.ProviderUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/11  12:41
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_INSTALL)
public class UtilsInstallServiceImpl implements CloudUtilsInstallService {

    /**
     * 添加安装状态回调
     *
     * @param cloudInstallCallback : 安装状态回调接口(保存方式：弱引用，需要注意回收问题)
     * @param statusEnums          : 准备接收的状态类型
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-24 17:06
     */
    @Override
    public void addInstallCallback(@NonNull CloudInstallCallback cloudInstallCallback, @Nullable CloudInstallStatusEnum... statusEnums) {
        if (statusEnums == null || statusEnums.length == 0) {
            return;
        }
        HashMap<String, CloudInstallCallback> hashMap = new HashMap<>(statusEnums.length);
        IntentFilter intentFilter = new IntentFilter();
        UtilsPermissionServiceImpl permissionService = new UtilsPermissionServiceImpl();
        for (CloudInstallStatusEnum statusEnum : statusEnums) {
            if (statusEnum == null) {
                continue;
            }
            if (statusEnum == CloudInstallStatusEnum.PACKAGE_ADDED) {
                if (!permissionService.isPermissionGranted(Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
                    Logger.Warning(CloudApiError.PERMISSION_DENIED.setAbout(Manifest.permission.REQUEST_INSTALL_PACKAGES).build());
                }
            }
            if (statusEnum == CloudInstallStatusEnum.PACKAGE_REMOVED) {
                if (!permissionService.isPermissionGranted(Manifest.permission.REQUEST_DELETE_PACKAGES)) {
                    Logger.Warning(CloudApiError.PERMISSION_DENIED.setAbout(Manifest.permission.REQUEST_DELETE_PACKAGES).build());
                }
            }
            hashMap.put(statusEnum.getValue(), cloudInstallCallback);
            intentFilter.addAction(statusEnum.getValue());
        }
        intentFilter.addDataScheme("package");
        UtilsBroadcastReceiver.getInstance().addIntentFilter(intentFilter, InstallReceiverListenerManager.getInstance().addMap(hashMap));
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
    @Override
    public boolean isInstallApp(@NonNull String packageName) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return false;
        }
        PackageManager packageManager = Cache.getPackageManager(context);
        if (packageManager == null) {
            return false;
        }
        try {
            return packageManager.getLaunchIntentForPackage(packageName) != null;
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        return false;
    }

    /**
     * 添加允许通过 provider 共享的文件路径，用于调起安装等
     * 如果不设置，默认所有路径都是安全路径，建议设置
     *
     * @param filePath : 允许共享的安全路径
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 13:30
     */
    @Override
    public void addProviderResourcePath(@NonNull String filePath) {
        UtilsProvider.addSecurityPaths(filePath);
        Logger.Info("add Security Path. : " + filePath);
    }

    /**
     * 安装应用
     *
     * @param apkPath : 安装包路径
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 09:55
     */
    @Override
    public void installApp(@NonNull String apkPath) {
        File file = new File(apkPath);
        if (!file.exists()) {
            Logger.Error(CloudApiError.DATA_EMPTY.setMsg("The apk file is empty. " + apkPath).build());
            return;
        }
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type = "application/vnd.android.package-archive";

        Uri uri;
        CloudUtilsAppService service = new UtilsAppServiceImpl();
        boolean isSdkVersionReady = Build.VERSION.SDK_INT < Build.VERSION_CODES.N;
        // 部分机型在系统版本为7.0，但使用provier形式会崩溃。所以判断如果targetV如果<=23并且系统版本为7.0时仍然使用file://形式
        boolean isTargetReady = Build.VERSION.SDK_INT == Build.VERSION_CODES.N && service.getTargetSdkVersion() <= Build.VERSION_CODES.M;
        if (isSdkVersionReady || isTargetReady) {
            uri = Uri.fromFile(file);
        } else {
            String provider = ProviderUtils.getProviderAuthoritiesFromManifest(UtilsProvider.class.getName(), "proxy_service_provider");
            uri = Uri.parse("content://" + provider + apkPath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, type);
        PackageManager packageManager = Cache.getPackageManager(context);
        if (packageManager == null) {
            return;
        }
        try {
            if (packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() <= 0) {
                Logger.Debug("install failed");
                return;
            }
            context.startActivity(intent);
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
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
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
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
            Logger.Debug(throwable);
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
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }
        UtilsPermissionServiceImpl permissionService = new UtilsPermissionServiceImpl();
        if (!permissionService.isPermissionGranted(Manifest.permission.REQUEST_DELETE_PACKAGES)) {
            Logger.Error(CloudApiError.PERMISSION_DENIED.setAbout(Manifest.permission.REQUEST_DELETE_PACKAGES).build());
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
    @NonNull
    @Override
    public List<CloudAppInfo> getAllInstallAppsInfo() {
        List<CloudAppInfo> infoList = new ArrayList<>();
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return infoList;
        }
        PackageManager packageManager = Cache.getPackageManager(context);
        if (packageManager == null) {
            return infoList;
        }

        List<PackageInfo> apps = packageManager.getInstalledPackages(0);

        for (PackageInfo packageInfo : apps) {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;

            CloudAppInfo appInfo = new CloudAppInfo();
            appInfo.icon = applicationInfo.loadIcon(packageManager);
            appInfo.name = applicationInfo.loadLabel(packageManager).toString();
            appInfo.packageName = packageInfo.packageName;
            appInfo.versionCode = packageInfo.versionCode;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appInfo.longVersionCode = packageInfo.getLongVersionCode();
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

    /**
     * 打开对应包名的app
     *
     * @param packageName : 包名
     * @return 是否打开成功，true 成功，false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 13:49
     */
    @Override
    public boolean openApp(@NonNull String packageName) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return false;
        }
        PackageManager packageManager = Cache.getPackageManager(context);
        if (packageManager == null) {
            return false;
        }
        try {
            Intent intent = packageManager.getLaunchIntentForPackage(packageName);
            if (intent == null) {
                Logger.Debug("open failed");
                return false;
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }

        return false;
    }

}
