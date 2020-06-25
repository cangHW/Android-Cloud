package com.proxy.service.library.info;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.cloud.annotations.CloudService;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsAppService;
import com.proxy.service.api.services.CloudUtilsSystemPageService;
import com.proxy.service.api.tag.CloudServiceTagLibrary;
import com.proxy.service.api.utils.Logger;

/**
 * @author: cangHX
 * on 2020/06/18  13:49
 */
@CloudService(serviceTag = CloudServiceTagLibrary.UTILS_SYSTEM_PAGE)
public class CloudUtilsSystemPageServiceImpl implements CloudUtilsSystemPageService {

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
            Logger.Error(CloudApiError.NO_INIT.build());
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        if (TextUtils.isEmpty(packageName)) {
            CloudUtilsAppService service = new CloudUtilsAppServiceImpl();
            intent.setData(Uri.parse("package:" + service.getPackageName()));
        } else {
            intent.setData(Uri.parse("package:" + packageName));
        }
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
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

        CloudUtilsAppService service = new CloudUtilsAppServiceImpl();

        if (TextUtils.isEmpty(packageName)) {
            intent.putExtra("app_package", service.getPackageName());
        } else {
            intent.putExtra("app_package", packageName);
        }

        if (TextUtils.isEmpty(packageName)) {
            intent.putExtra("app_uid", service.getUid());
        } else {
            intent.putExtra("app_uid", uid);
        }

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
            Logger.Error(CloudApiError.NO_INIT.build());
            return;
        }
        Intent intent = new Intent();
        if (TextUtils.isEmpty(phoneNumber)) {
            intent.setAction(Intent.ACTION_CALL_BUTTON);
        } else {
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
        }
        context.startActivity(intent);
    }


}
