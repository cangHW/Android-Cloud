package com.proxy.service.utils.info;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;

import androidx.core.app.AppOpsManagerCompat;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.permission.IPermissionCallback;
import com.proxy.service.api.services.CloudUtilsPermissionService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.cache.Cache;
import com.proxy.service.utils.permission.PermissionCallbackImpl;

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
        if (TextUtils.isEmpty(permission)) {
            Logger.Error(CloudApiError.DATA_EMPTY.setMsg("permission is not be null").build());
            return false;
        }

        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return false;
        }

        int uid = Process.myUid();
        String packageName = context.getPackageName();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String op = AppOpsManagerCompat.permissionToOp(permission);
            if (op == null) {
                int pid = Process.myPid();
                return context.checkPermission(permission, pid, uid) != PackageManager.PERMISSION_DENIED;
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

            if (TextUtils.isEmpty(packageName)) {
                return false;
            }
            return AppOpsManagerCompat.noteProxyOpNoThrow(context, op, packageName) == AppOpsManagerCompat.MODE_ALLOWED;
        }

        int pid = Process.myPid();
        return context.checkPermission(permission, pid, uid) != PackageManager.PERMISSION_DENIED;
    }

    /**
     * 申请对应权限
     *
     * @return 返回请求权限处理对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/31 9:20 PM
     */
    @Override
    public IPermissionCallback requestPermission() {
        return new PermissionCallbackImpl();
    }
}
