package com.proxy.service.utils.network;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.proxy.service.api.net.CloudNetWorkCallback;
import com.proxy.service.api.net.CloudNetWorkType;
import com.proxy.service.api.services.CloudUtilsNetWorkService;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.log.WeakReferenceUtils;
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

    private final CloudUtilsTaskService mTaskService;
    private final CloudUtilsNetWorkService mNetWorkService;
    private final NetworkCallback mNetworkCallback;
    private ConnectivityManager mManager;

    private NetWorkReceiverListenerManager() {
        mTaskService = new UtilsTaskServiceImpl();
        mNetWorkService = new UtilsNetWorkServiceImpl();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mNetworkCallback = new NetworkCallback(this);
        } else {
            mNetworkCallback = null;
        }
    }

    private static class Factory {
        private static final NetWorkReceiverListenerManager INSTANCE = new NetWorkReceiverListenerManager();
    }

    public static NetWorkReceiverListenerManager getInstance() {
        return Factory.INSTANCE;
    }

    public void setConnectivityManager(ConnectivityManager manager) {
        this.mManager = manager;
    }

    public ConnectivityManager.NetworkCallback getNetworkCallback() {
        return mNetworkCallback;
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
                    public void onCallback(WeakReference<CloudNetWorkCallback> weakReference, CloudNetWorkCallback netWorkCallback) {
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static class NetworkCallback extends ConnectivityManager.NetworkCallback {

        private final WeakReference<NetWorkReceiverListenerManager> weakReference;

        public NetworkCallback(NetWorkReceiverListenerManager netWorkReceiverListenerManager) {
            weakReference = new WeakReference<>(netWorkReceiverListenerManager);
        }

        @Override
        public void onAvailable(@NonNull final Network network) {
            super.onAvailable(network);
            WeakReferenceUtils.checkValueIsEmpty(weakReference, new WeakReferenceUtils.Callback<NetWorkReceiverListenerManager>() {
                @Override
                public void onCallback(WeakReference<NetWorkReceiverListenerManager> weakReference, final NetWorkReceiverListenerManager netWorkReceiverListenerManager) {
                    netWorkReceiverListenerManager.mTaskService.callUiThread(new Task<Object>() {
                        @Override
                        public Object call() {
                            WeakReferenceUtils.checkValueIsEmpty(netWorkReceiverListenerManager.mNetWorkCallbacks, new WeakReferenceUtils.Callback<CloudNetWorkCallback>() {
                                @Override
                                public void onCallback(WeakReference<CloudNetWorkCallback> weakReference, CloudNetWorkCallback netWorkCallback) {
                                    NetworkCapabilities networkCapabilities = netWorkReceiverListenerManager.mManager.getNetworkCapabilities(network);
                                    if (networkCapabilities == null) {
                                        netWorkCallback.onReceive(CloudNetWorkType.UNKNOWN);
                                        return;
                                    }
                                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                                        netWorkCallback.onReceive(CloudNetWorkType.WIFI);
                                        return;
                                    }
                                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                                        netWorkCallback.onReceive(CloudNetWorkType.MOBILE);
                                        return;
                                    }
                                    netWorkCallback.onReceive(CloudNetWorkType.UNKNOWN);
                                }
                            });
                            return null;
                        }
                    });
                }
            });
        }

        @Override
        public void onLost(@NonNull Network network) {
            WeakReferenceUtils.checkValueIsEmpty(weakReference, new WeakReferenceUtils.Callback<NetWorkReceiverListenerManager>() {
                @Override
                public void onCallback(WeakReference<NetWorkReceiverListenerManager> weakReference, final NetWorkReceiverListenerManager netWorkReceiverListenerManager) {
                    netWorkReceiverListenerManager.mTaskService.callUiThread(new Task<Object>() {
                        @Override
                        public Object call() {
                            WeakReferenceUtils.checkValueIsEmpty(netWorkReceiverListenerManager.mNetWorkCallbacks, new WeakReferenceUtils.Callback<CloudNetWorkCallback>() {
                                @Override
                                public void onCallback(WeakReference<CloudNetWorkCallback> weakReference, CloudNetWorkCallback netWorkCallback) {
                                    netWorkCallback.disConnect();
                                }
                            });
                            return null;
                        }
                    });
                }
            });
        }
    }
}
