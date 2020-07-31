package com.proxy.service.network.factory;

import android.text.TextUtils;

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
    }

    public void setBuilder(Builder builder) {
        if (!TextUtils.isEmpty(builder.baseUrl)) {
            mBuilder.baseUrl(builder.baseUrl);
        }
    }

    public void setClient(OkHttpClient client) {
        mBuilder.client(client);
    }

    public Retrofit getRetrofit() {
        return mBuilder.build();
    }

    public static class Builder {
        String baseUrl;

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }

}
