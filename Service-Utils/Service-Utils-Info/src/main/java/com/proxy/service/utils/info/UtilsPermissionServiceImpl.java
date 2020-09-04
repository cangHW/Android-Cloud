package com.proxy.service.utils.info;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;

import androidx.core.app.AppOpsManagerCompat;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsAppService;
import com.proxy.service.api.services.CloudUtilsPermissionService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.cache.Cache;

/**
 * @author : cangHX
 * on 2020/08/31  9:22 PM
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_PERMISSION)
public class UtilsPermissionServiceImpl implements CloudUtilsPermissionService {
    /**
     * 是否具有对应权限
     *
     * @param permission : 权限名称
     * @return true 有，false 没有
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/31 9:19 PM
     */
    @Override
    public boolean isPermissionGranted(String permission) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return false;
        }
        int pid = Process.myPid();
        int uid = Process.myUid();
        String packageName = context.getPackageName();

        if (context.checkPermission(permission, pid, uid) == PackageManager.PERMISSION_DENIED) {
            return false;
        }

        String op = AppOpsManagerCompat.permissionToOp(permission);
        if (op == null) {
            return true;
        }

        if (packageName == null) {
            PackageManager packageManager = Cache.getPackageManager(context);
            if (packageManager != null) {
                String[] packageNames = packageManager.getPackagesForUid(uid);
                if (packageNames == null || packageNames.length <= 0) {
                    return false;
                }
                packageName = packageNames[0];
            }
        }

        return AppOpsManagerCompat.noteProxyOp(context, op, packageName) == AppOpsManagerCompat.MODE_ALLOWED;
    }

    /**
     * 自动获取对应权限
     *
     * @param permission : 权限名称
     * @return true 有，false 没有
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/31 9:20 PM
     */
    @Override
    public boolean selfPermissionGranted(String permission) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return false;
        }
        CloudUtilsAppService appService = new UtilsAppServiceImpl();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (appService.getTargetSdkVersion() >= Build.VERSION_CODES.M) {
                return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
            }
            return isPermissionGranted(permission);
        }
        PackageManager packageManager = Cache.getPackageManager(context);
        if (packageManager != null) {
            return packageManager.checkPermission(permission, context.getPackageName()) != PackageManager.PERMISSION_DENIED;
        }
        return true;
    }
}
