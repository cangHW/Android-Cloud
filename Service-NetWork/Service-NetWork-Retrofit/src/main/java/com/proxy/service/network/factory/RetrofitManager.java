package com.proxy.service.network.factory;

import retrofit2.Retrofit;

/**
 * @author : cangHX
 * on 2020/07/21  7:51 PM
 */
public class RetrofitManager {

    private Retrofit mRetrofit;

    private RetrofitManager() {
        RetrofitFactory.getInstance().setClient(OkHttpFactory.getInstance().getOkHttpClient());
        mRetrofit = RetrofitFactory.getInstance().getRetrofit();
    }

    private static class Factory {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    public static RetrofitManager getInstance() {
        return Factory.INSTANCE;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}
