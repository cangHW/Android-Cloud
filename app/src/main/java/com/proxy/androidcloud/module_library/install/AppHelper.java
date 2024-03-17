package com.proxy.androidcloud.module_library.install;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.androidcloud.helper.AbstractListHelper;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.install.CloudAppInfo;
import com.proxy.service.api.install.CloudInstallCallback;
import com.proxy.service.api.install.CloudInstallStatusEnum;
import com.proxy.service.api.services.CloudUtilsInstallService;
import com.proxy.service.api.services.CloudUtilsSystemService;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.tag.CloudServiceTagUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/10/16  10:33 PM
 */
public class AppHelper extends AbstractListHelper implements CloudInstallCallback {

    private final CloudUtilsInstallService service;
    private final CloudUtilsSystemService pageService;
    private CloudUtilsTaskService taskService;
    private List<CloudAppInfo> appInfos = new ArrayList<>();

    public AppHelper() {
        service = CloudSystem.getService(CloudServiceTagUtils.UTILS_INSTALL);
        pageService = CloudSystem.getService(CloudServiceTagUtils.UTILS_SYSTEM_PAGE);
        taskService = CloudSystem.getService(CloudServiceTagUtils.UTILS_TASK);
        if (service == null) {
            return;
        }
        service.addInstallCallback(this, CloudInstallStatusEnum.PACKAGE_REMOVED);
    }

    private void initData() {
        if (taskService == null) {
            return;
        }
        taskService.callWorkThread(() -> {
            appInfos = service.getAllInstallAppsInfo();
            taskService.callUiThread(() -> {
                refresh();
                return null;
            });
            return null;
        });
    }

    @Override
    public List<HelperItemInfo> createItems() {
        List<HelperItemInfo> infos = new ArrayList<>();
        List<CloudAppInfo> apps = new ArrayList<>(appInfos);
        for (int i = 0; i < apps.size(); i++) {
            CloudAppInfo appInfo = apps.get(i);
            HelperItemInfo.Builder builder = HelperItemInfo.builder();
            builder.setTitle(appInfo.name + "\n" + appInfo.packageName + "\n" + appInfo.versionName + "\n" + (appInfo.isSystemApp ? "系统应用" : "普通应用"));
            if (!appInfo.isSystemApp) {
                builder.setRightButton("卸载");
                builder.setCenterButton("打开");
            }
            builder.setLeftButton("查看详情");
            builder.setId(i);
            infos.add(builder.build());
        }
        if (appInfos.size() == 0) {
            initData();
        }
        return infos;
    }

    @Override
    public void onItemClick(Context context, HelperItemInfo itemInfo, int button) {
        if (service == null) {
            return;
        }
        if (appInfos.size() <= itemInfo.id) {
            return;
        }
        CloudAppInfo appInfo = appInfos.get(itemInfo.id);
        if (button == HelperItemInfo.BUTTON_RIGHT) {
            service.unInstallApp(appInfo.packageName);
        } else if (button == HelperItemInfo.BUTTON_LEFT) {
            if (pageService == null) {
                return;
            }
            pageService.openAppSetting(appInfo.packageName);
        } else {
            service.openApp(appInfo.packageName);
        }
    }

    /**
     * 安装状态变化
     *
     * @param packageName            : 包名
     * @param cloudInstallStatusEnum : 安装状态
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-24 17:10
     */
    @Override
    public void onStatusChanged(@Nullable String packageName, @NonNull CloudInstallStatusEnum cloudInstallStatusEnum) {
        List<CloudAppInfo> apps = new ArrayList<>(appInfos);
        for (CloudAppInfo app : apps) {
            if (app.packageName.equals(packageName)) {
                appInfos.remove(app);
                refresh();
                return;
            }
        }
    }
}
