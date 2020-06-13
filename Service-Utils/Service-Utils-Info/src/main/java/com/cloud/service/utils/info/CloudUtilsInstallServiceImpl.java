package com.cloud.service.utils.info;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;

import com.cloud.annotations.CloudService;
import com.cloud.api.context.ContextManager;
import com.cloud.api.error.CloudApiError;
import com.cloud.api.services.CloudUtilsInstallService;
import com.cloud.api.tag.CloudServiceTagUtils;
import com.cloud.api.utils.Logger;
import com.cloud.service.utils.cache.Cache;
import com.cloud.service.utils.provider.CloudProvider;
import com.cloud.service.utils.util.Utils;

import java.io.File;
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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            String provider = Utils.getProviderAuthoritiesFromManifest(CloudProvider.class.getName(), "cloud_service_provider");
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
                return;
            }
            context.startActivity(intent);
        } catch (Throwable ignored) {
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
        return null;
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
    @NonNull
    @Override
    public List<AppInfo> getAllInstallAppsInfo() {
        return null;
    }

    /**
     * 打开应用设置页面
     *
     * @param packageName : 包名
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:11
     */
    @Override
    public void openAppSetting(@NonNull String packageName) {
        Context context = ContextManager.getCurrentActivity();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }
}
