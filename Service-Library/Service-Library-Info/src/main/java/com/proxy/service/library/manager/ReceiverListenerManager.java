package com.proxy.service.library.manager;

import android.content.Context;
import android.content.Intent;

import com.proxy.service.api.callback.CloudInstallCallback;
import com.proxy.service.library.receiver.CloudBroadcastReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: cangHX
 * on 2020/06/24  18:47
 */
public class ReceiverListenerManager implements CloudBroadcastReceiver.ReceiverListener {

    private HashMap<String, List<CloudInstallCallback>> mCallbackMap = new HashMap<>();

    private static class Factory {
        private static ReceiverListenerManager mInstance = new ReceiverListenerManager();
    }

    public static ReceiverListenerManager getInstance() {
        return Factory.mInstance;
    }

    public ReceiverListenerManager addMap(HashMap<String, CloudInstallCallback> hashMap) {
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
        List<CloudInstallCallback> list = mCallbackMap.get(action);
        if (list == null || list.size() == 0) {
            return;
        }
        for (CloudInstallCallback callback : list) {
            //TODO
//            callback.onStatusChanged();
        }
    }
}
