package com.proxy.service.utils.install;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.proxy.service.api.install.CloudInstallCallback;
import com.proxy.service.api.install.CloudInstallStatusEnum;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.api.utils.WeakReferenceUtils;
import com.proxy.service.utils.info.UtilsTaskServiceImpl;
import com.proxy.service.utils.receiver.UtilsBroadcastReceiver;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: cangHX
 * on 2020/06/24  18:47
 */
public class InstallReceiverListenerManager implements UtilsBroadcastReceiver.ReceiverListener {

    private final HashMap<String, List<WeakReference<CloudInstallCallback>>> mCallbackMap = new HashMap<>();

    private final CloudUtilsTaskService mTaskService;

    private static class Factory {
        private static final InstallReceiverListenerManager M_INSTANCE = new InstallReceiverListenerManager();
    }

    private InstallReceiverListenerManager() {
        mTaskService = new UtilsTaskServiceImpl();
    }

    public static InstallReceiverListenerManager getInstance() {
        return Factory.M_INSTANCE;
    }

    public InstallReceiverListenerManager addMap(HashMap<String, CloudInstallCallback> hashMap) {
        for (Map.Entry<String, CloudInstallCallback> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            CloudInstallCallback value = entry.getValue();

            List<WeakReference<CloudInstallCallback>> list = mCallbackMap.get(key);
            if (list == null) {
                list = new ArrayList<>();
                mCallbackMap.put(key, list);
            }
            list.add(new WeakReference<>(value));
        }
        return this;
    }

    /**
     * 接收到消息
     *
     * @param context : 上下文环境
     * @param intent  : 意图
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-24 18:04
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        final CloudInstallStatusEnum cloudInstallStatusEnum = CloudInstallStatusEnum.of(action);
        if (cloudInstallStatusEnum == null) {
            return;
        }
        Uri uri = intent.getData();
        String packageName = null;
        if (uri != null) {
            packageName = uri.getSchemeSpecificPart();
        }
        final String finalPackageName = packageName;
        List<WeakReference<CloudInstallCallback>> list = mCallbackMap.get(action);
        WeakReferenceUtils.checkValueIsEmpty(list, new WeakReferenceUtils.Callback<CloudInstallCallback>() {
            @Override
            public void onCallback(WeakReference<CloudInstallCallback> weakReference, final CloudInstallCallback cloudInstallCallback) {
                mTaskService.callUiThread(new Task<Object>() {
                    @Override
                    public Object call() {
                        try {
                            cloudInstallCallback.onStatusChanged(finalPackageName, cloudInstallStatusEnum);
                        } catch (Throwable throwable) {
                            Logger.Debug(throwable);
                        }
                        return null;
                    }
                });
            }
        });
    }
}
