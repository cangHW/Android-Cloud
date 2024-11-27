package com.proxy.androidcloud.module_library.install;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.base.BaseApplication;
import com.proxy.androidcloud.helper.AbstractDetailHelper;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.install.CloudInstallCallback;
import com.proxy.service.api.install.CloudInstallStatusEnum;
import com.proxy.service.api.services.CloudUtilsInstallService;
import com.proxy.service.api.log.Logger;

import java.io.File;

/**
 * @author : cangHX
 * on 2020/09/27  10:22 PM
 */
public class InstallHelper extends AbstractDetailHelper {

    private static final String SECURITY_PATH = "/storage/emulated/0/Android/data/com.chx.androidcloud/files";
    private static final String APK_PATH = "/storage/emulated/0/Android/data/com.chx.androidcloud/files/app-develop-release.apk";

    private static final Logger mLogger = Logger.create("InstallHelper");

    private CloudUtilsInstallService service;
    private String pkg;

    @Override
    public int getLayoutId() {
        if (service == null) {
            service = CloudSystem.getService(CloudUtilsInstallService.class);
        }
        if (service != null) {
            service.addInstallCallback(
                    installCallback,
                    CloudInstallStatusEnum.PACKAGE_ADDED,
                    CloudInstallStatusEnum.PACKAGE_REMOVED
            );
        }
        return R.layout.item_install;
    }

    @Override
    public void init(Activity activity) {
        if (service == null) {
            return;
        }
        EditText mSecurityPath = activity.findViewById(R.id.mSecurityPath);
        Button mSecurityButton = activity.findViewById(R.id.mSecurityButton);
        Button mInstallButton = activity.findViewById(R.id.mInstallButton);
        EditText mPkg = activity.findViewById(R.id.mPkg);
        Button mDeleteButton = activity.findViewById(R.id.mDeleteButton);

        mSecurityPath.setText(SECURITY_PATH);
        mSecurityButton.setOnClickListener(v -> {
            service.addProviderResourcePath(mSecurityPath.getText().toString());
        });

        mInstallButton.setOnClickListener(v -> {
            File file = new File(APK_PATH);
            if (!file.exists()) {
                Toast.makeText(activity, "请去网络页面下载安装包", Toast.LENGTH_SHORT).show();
                return;
            }

            pkg = service.getPackageName(APK_PATH);
            if (service.isInstallApp(pkg)) {
                Toast.makeText(activity, "应用已安装", Toast.LENGTH_SHORT).show();
                service.openApp(pkg);
            } else {
                //设置安全路径，允许此路径下的文件进行分享
                service.addProviderResourcePath(APK_PATH);
                //发起安装
                service.installApp(APK_PATH);
            }
        });

        mPkg.setText("com.qihoo.appstore");
        mDeleteButton.setOnClickListener(v -> {
            String pkg = mPkg.getText().toString();
            if (TextUtils.isEmpty(pkg)) {
                Toast.makeText(activity, "请输入包名", Toast.LENGTH_SHORT).show();
                return;
            }

            if (service.isInstallApp(pkg)) {
                service.unInstallApp(pkg);
            } else {
                Toast.makeText(activity, "应用未安装", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private CloudInstallCallback installCallback = new CloudInstallCallback() {

        @Override
        public void onStatusChanged(@Nullable String packageName, @NonNull CloudInstallStatusEnum cloudInstallStatusEnum) {
            mLogger.debug("包名 : " + packageName);
            //需要添加包名判断，确定回调是否由自己发起
            if (cloudInstallStatusEnum == CloudInstallStatusEnum.PACKAGE_ADDED) {
                Toast.makeText(BaseApplication.getInstance(), "安装完成", Toast.LENGTH_SHORT).show();
                service.openApp(pkg);
            } else if (cloudInstallStatusEnum == CloudInstallStatusEnum.PACKAGE_REMOVED) {
                Toast.makeText(BaseApplication.getInstance(), "卸载完成", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
