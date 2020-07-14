package com.proxy.service.utils.install;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.proxy.service.api.callback.CloudInstallCallback;
import com.proxy.service.api.enums.CloudInstallStatusEnum;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.receiver.UtilsBroadcastReceiver;
import com.proxy.service.utils.util.HandleUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: cangHX
 * on 2020/06/24  18:47
 */
public class InstallReceiverListenerManager implements UtilsBroadcastReceiver.ReceiverListener {

    private final HashMap<String, List<CloudInstallCallback>> mCallbackMap = new HashMap<>();

    private static class Factory {
        private static final InstallReceiverListenerManager M_INSTANCE = new InstallReceiverListenerManager();
    }

    public static InstallReceiverListenerManager getInstance() {
        return Factory.M_INSTANCE;
    }

    public InstallReceiverListenerManager addMap(HashMap<String, CloudInstallCallback> hashMap) {
        for (Map.Entry<String, CloudInstallCallback> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            CloudInstallCallback value = entry.getValue();

            List<CloudInstallCallback> list = mCallbackMap.get(key);
            if (list == null) {
                list = new ArrayList<>();
                mCallbackMap.put(key, list);
            }
            list.add(value);
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
        List<CloudInstallCallback> list = mCallbackMap.get(action);
        if (list == null || list.size() == 0) {
            return;
        }
        final CloudInstallStatusEnum cloudInstallStatusEnum = CloudInstallStatusEnum.of(action);
        if (cloudInstallStatusEnum == null) {
            return;
        }
        for (final CloudInstallCallback callback : list) {
            HandleUtils.postMain(new Runnable() {
                @Override
                public void run() {
                    try {
                        callback.onStatusChanged(cloudInstallStatusEnum);
                    } catch (Throwable throwable) {
                        Logger.Debug(throwable);
                    }
                }
            });
        }
    }
}
