package com.proxy.service.api.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.callback.request.CloudNetWorkCall;
import com.proxy.service.api.callback.request.CloudNetWorkCallback;
import com.proxy.service.api.callback.request.CloudNetWorkGlobalCallback;
import com.proxy.service.api.callback.response.CloudNetWorkResponse;

import java.util.ArrayList;

/**
 * @author : cangHX
 * on 2020/08/05  10:12 PM
 */
public class CallbackManager {

    private static final ArrayList<CloudNetWorkGlobalCallback> CALLBACKS = new ArrayList<>();

    public static void addGlobalCallback(CloudNetWorkGlobalCallback callback) {
        if (callback == null) {
            return;
        }
        CALLBACKS.add(callback);
    }

    public static void start(@NonNull CloudNetWorkCall<?> call) {
        ArrayList<CloudNetWorkGlobalCallback> list = new ArrayList<>(CALLBACKS);
        for (CloudNetWorkGlobalCallback callback : list) {
            callback.onStart(call);
        }
    }

    public static <T> void response(@NonNull CloudNetWorkCall<T> call, @NonNull CloudNetWorkResponse<T> response, @Nullable CloudNetWorkCallback<T> callback) {
        ArrayList<CloudNetWorkGlobalCallback> list = new ArrayList<>(CALLBACKS);
        for (CloudNetWorkGlobalCallback globalCallback : list) {
            if (globalCallback.onResponse(call, response)) {
                return;
            }
        }
        if (callback != null) {
            callback.onResponse(response);
        }
    }

    public static <T> void failure(@NonNull CloudNetWorkCall<T> call, @NonNull Throwable t, @Nullable CloudNetWorkCallback<T> callback) {
        ArrayList<CloudNetWorkGlobalCallback> list = new ArrayList<>(CALLBACKS);
        for (CloudNetWorkGlobalCallback globalCallback : list) {
            if (globalCallback.onFailure(call, t)) {
                return;
            }
        }
        if (callback != null) {
            callback.onFailure(t);
        }
    }

    public static void finish(@NonNull CloudNetWorkCall<?> call) {
        ArrayList<CloudNetWorkGlobalCallback> list = new ArrayList<>(CALLBACKS);
        for (CloudNetWorkGlobalCallback callback : list) {
            callback.onFinish(call);
        }
    }
}
