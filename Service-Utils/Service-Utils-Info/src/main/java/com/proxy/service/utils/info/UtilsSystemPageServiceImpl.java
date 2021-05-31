package com.proxy.service.utils.info;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsAppService;
import com.proxy.service.api.services.CloudUtilsSystemPageService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;

/**
 * @author: cangHX
 * on 2020/06/18  13:49
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_SYSTEM_PAGE)
public class UtilsSystemPageServiceImpl implements CloudUtilsSystemPageService {

    /**
     * 打开应用设置页面
     *
     * @param packageName : 包名，为空默认使用当前app的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:11
     */
    @Override
    public void openAppSetting(@Nullable String packageName) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }

        if (TextUtils.isEmpty(packageName)) {
            CloudUtilsAppService service = new UtilsAppServiceImpl();
            packageName = service.getPackageName();
        }

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }

    /**
     * 打开应用通知设置页面
     *
     * @param packageName : 包名，为空默认使用当前app的数据
     * @param uid         : 应用的uid，为空默认使用当前app的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:11
     */
    @Override
    public void openNotificationSetting(@Nullable String packageName, @Nullable String uid) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            openAppSetting(packageName);
            return;
        }

        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }

        CloudUtilsAppService service = new UtilsAppServiceImpl();
        if (TextUtils.isEmpty(packageName)) {
            packageName = service.getPackageName();
        }
        if (TextUtils.isEmpty(uid)) {
            uid = service.getUid();
        }

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName);
        intent.putExtra("app_package", packageName);
        intent.putExtra("app_uid", uid);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 打电话
     *
     * @param phoneNumber : 准备拨打的电话号码
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-18 14:26
     */
    @Override
    public void openCall(@Nullable String phoneNumber) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }
        Intent intent = new Intent();
        if (TextUtils.isEmpty(phoneNumber)) {
            intent.setAction(Intent.ACTION_CALL_BUTTON);
        } else {
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
