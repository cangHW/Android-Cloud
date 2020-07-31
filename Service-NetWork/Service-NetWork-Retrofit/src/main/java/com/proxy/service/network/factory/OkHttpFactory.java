package com.proxy.service.network.factory;

import com.proxy.service.network.interceptors.MockInterceptor;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * @author : cangHX
 * on 2020/07/21  8:28 PM
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
        mBuilder.addInterceptor(new MockInterceptor());
        if (builder.proxy != null) {
            mBuilder.proxy(builder.proxy);
        }
        if (builder.cookieJar != null) {
            mBuilder.cookieJar(builder.cookieJar);
        }
        if (builder.cache != null) {
            mBuilder.cache(builder.cache);
        }
        if (builder.sslSocketFactory != null && builder.manager != null) {
            mBuilder.sslSocketFactory(builder.sslSocketFactory, builder.manager);
        }
    }

    public OkHttpClient getOkHttpClient() {
        return mBuilder.build();
    }

    public static class Builder {
        int readTimeout = -1;
        int writeTimeout = -1;
        int connectTimeout = -1;
        Proxy proxy;
        CookieJar cookieJar;
        Cache cache;
        SSLSocketFactory sslSocketFactory;
        X509TrustManager manager;
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

        public void setProxy(Proxy proxy) {
            this.proxy = proxy;
        }

        public void setCookieJar(CookieJar cookieJar) {
            this.cookieJar = cookieJar;
        }

        public void setCache(Cache cache) {
            this.cache = cache;
        }

        public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
        }

        public void setX509TrustManager(X509TrustManager manager) {
            this.manager = manager;
        }
    }
}
