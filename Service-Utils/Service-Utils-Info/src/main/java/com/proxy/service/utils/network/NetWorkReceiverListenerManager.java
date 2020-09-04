package com.proxy.service.utils.network;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.proxy.service.api.net.CloudNetWorkCallback;
import com.proxy.service.api.services.CloudUtilsNetWorkService;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.utils.WeakReferenceUtils;
import com.proxy.service.utils.info.UtilsNetWorkServiceImpl;
import com.proxy.service.utils.info.UtilsTaskServiceImpl;
import com.proxy.service.utils.receiver.UtilsBroadcastReceiver;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/08/31  3:33 PM
 */
public class NetWorkReceiverListenerManager implements UtilsBroadcastReceiver.ReceiverListener {

    private final List<WeakReference<CloudNetWorkCallback>> mNetWorkCallbacks = new ArrayList<>();

    private CloudUtilsTaskService mTaskService;
    private CloudUtilsNetWorkService mNetWorkService;

    private NetWorkReceiverListenerManager() {
        mTaskService = new UtilsTaskServiceImpl();
        mNetWorkService = new UtilsNetWorkServiceImpl();
    }

    private static class Factory {
        private static final NetWorkReceiverListenerManager INSTANCE = new NetWorkReceiverListenerManager();
    }

    public static NetWorkReceiverListenerManager getInstance() {
        return Factory.INSTANCE;
    }

    public NetWorkReceiverListenerManager addNetWorkCallback(CloudNetWorkCallback netWorkCallback) {
        boolean hasSame = WeakReferenceUtils.checkValueIsSame(mNetWorkCallbacks, netWorkCallback);
        if (!hasSame) {
            mNetWorkCallbacks.add(new WeakReference<>(netWorkCallback));
        }
        return this;
    }

    public void removeNetWorkCallback(CloudNetWorkCallback netWorkCallback) {
        for (WeakReference<CloudNetWorkCallback> weakReference : mNetWorkCallbacks) {
            if (weakReference == null) {
                continue;
            }
            CloudNetWorkCallback callback = weakReference.get();
            if (callback == netWorkCallback) {
                weakReference.clear();
            }
        }
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
    public void onReceive(final Context context, final Intent intent) {
        String action = intent.getAction();
        if (!ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            return;
        }
        mTaskService.callUiThread(new Task<Object>() {
            @Override
            public Object call() {
                WeakReferenceUtils.checkValueIsEmpty(mNetWorkCallbacks, new WeakReferenceUtils.Callback<CloudNetWorkCallback>() {
                    @Override
                    public void onCallback(CloudNetWorkCallback netWorkCallback) {
                        if (mNetWorkService.isConnected()) {
                            netWorkCallback.onReceive(mNetWorkService.getNetworkType());
                        } else {
                            netWorkCallback.disConnect();
                        }
                    }
                });
                return null;
            }
        });
    }
}
