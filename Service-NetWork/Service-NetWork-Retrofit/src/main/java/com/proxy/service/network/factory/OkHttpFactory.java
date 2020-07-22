package com.proxy.service.network.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * @author : cangHX
 * on 2020/07/21  5:28 PM
 */
public enum OkHttpFactory {

    /**
     * 单例
     */
    INSTANCE;

    private OkHttpClient.Builder mBuilder;

    OkHttpFactory() {
        mBuilder = new OkHttpClient.Builder();
    }

    public void setBuilder(Builder builder) {
        if (builder.readTimeout > 0) {
            mBuilder.readTimeout(builder.readTimeout, TimeUnit.MILLISECONDS);
        }
        if (builder.writeTimeout > 0) {
            mBuilder.readTimeout(builder.writeTimeout, TimeUnit.MILLISECONDS);
        }
        if (builder.connectTimeout > 0) {
            mBuilder.readTimeout(builder.connectTimeout, TimeUnit.MILLISECONDS);
        }
        for (Interceptor interceptor : builder.interceptors) {
            mBuilder.addInterceptor(interceptor);
        }
    }

    public OkHttpClient getOkHttpClient() {
        return mBuilder.build();
    }

    public static class Builder {
        int readTimeout = -1;
        int writeTimeout = -1;
        int connectTimeout = -1;

        List<Interceptor> interceptors = new ArrayList<>();

        public void setReadTimeout(int timeout) {
            this.readTimeout = timeout;
        }

        public void setWriteTimeout(int timeout) {
            this.writeTimeout = timeout;
        }

        public void setConnectTimeout(int timeout) {
            this.connectTimeout = timeout;
        }

        public void addInterceptor(Interceptor interceptor) {
            this.interceptors.add(interceptor);
        }
    }
}
