package com.proxy.service.network.factory;

import retrofit2.Retrofit;

/**
 * @author : cangHX
 * on 2020/07/21  5:51 PM
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

    public Retrofit getRetrofit(){
        return mRetrofit;
    }
}
