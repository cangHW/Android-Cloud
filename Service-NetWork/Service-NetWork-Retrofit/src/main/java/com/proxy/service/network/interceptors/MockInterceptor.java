package com.proxy.service.network.interceptors;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author : cangHX
 * on 2020/7/23 9:03 PM
 */
public class MockInterceptor implements Interceptor {

    public MockInterceptor() {
    }

    @Override
    @NonNull
    public Response intercept(@NonNull Chain chain) throws IOException {
        return null;
    }
}
