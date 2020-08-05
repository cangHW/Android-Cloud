package com.proxy.service.network.factory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author : cangHX
 * on 2020/07/21  7:59 PM
 */
public enum RetrofitFactory {

    /**
     * 单例
     */
    INSTANCE;

    private Retrofit.Builder mBuilder;

    RetrofitFactory() {
        mBuilder = new Retrofit.Builder();
        mBuilder.baseUrl("");
    }

    public void setClient(OkHttpClient client) {
        mBuilder.client(client);
    }

    public Retrofit getRetrofit() {
        return mBuilder.build();
    }

}
