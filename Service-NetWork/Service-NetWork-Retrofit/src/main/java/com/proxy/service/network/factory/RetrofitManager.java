package com.proxy.service.network.factory;

import androidx.annotation.NonNull;

import com.proxy.service.api.method.ServiceMethod;
import com.proxy.service.api.services.CloudNetWorkRequestService;

import retrofit2.Retrofit;

/**
 * @author : cangHX
 * on 2020/07/21  7:51 PM
 */
public enum RetrofitManager {

    /**
     * 单例
     */
    INSTANCE;

    private Retrofit mRetrofit;

    RetrofitManager() {
        RetrofitFactory.INSTANCE.setClient(OkHttpFactory.INSTANCE.getOkHttpClient());
        mRetrofit = RetrofitFactory.INSTANCE.getRetrofit();
    }

    @NonNull
    public Object startRequest(boolean isService, ServiceMethod serviceMethod, CloudNetWorkRequestService service, String tag) {
        //todo 发起请求
        return null;
    }
}
