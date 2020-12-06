package com.proxy.service.network.factory;

import com.proxy.service.network.converter.StringConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author : cangHX
 * on 2020/07/21  7:59 PM
 */
public class RetrofitFactory {

    private final Retrofit.Builder mBuilder;

    private RetrofitFactory() {
        mBuilder = new Retrofit.Builder();
        mBuilder.baseUrl("https://github.com/cangHW/Android-Cloud/");
        mBuilder.addConverterFactory(StringConverterFactory.create());
    }

    private static class Factory {
        private static final RetrofitFactory INSTANCE = new RetrofitFactory();
    }

    public static RetrofitFactory getInstance() {
        return Factory.INSTANCE;
    }

    public void setClient(OkHttpClient client) {
        mBuilder.client(client);
    }

    public Retrofit getRetrofit() {
        return mBuilder.build();
    }
}
