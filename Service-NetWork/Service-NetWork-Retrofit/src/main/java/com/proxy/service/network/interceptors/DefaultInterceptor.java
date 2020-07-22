package com.proxy.service.network.interceptors;

import androidx.annotation.NonNull;

import com.proxy.service.api.base.CloudNetWorkInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author : cangHX
 * on 2020/07/21  5:59 PM
 */
public class DefaultInterceptor implements Interceptor {

    public DefaultInterceptor(CloudNetWorkInterceptor interceptor) {

    }

    @Override
    @NonNull
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        return chain.proceed(request);
    }
}
