package com.proxy.service.api.impl;

import androidx.annotation.NonNull;

import com.proxy.service.api.base.CloudNetWorkInterceptor;
import com.proxy.service.api.callback.response.CloudNetWorkResponse;
import com.proxy.service.api.method.CloudNetWorkRequest;

/**
 * @author : cangHX
 * on 2020/08/04  9:27 PM
 */
public class MockInterceptor implements CloudNetWorkInterceptor {
    /**
     * 拦截器执行体
     *
     * @param chain : 链条
     * @return 返回体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/4 10:18 PM
     */
    @Override
    public <T> CloudNetWorkResponse<T> intercept(@NonNull Chain chain) {
        CloudNetWorkRequest request = chain.request();
        return chain.proceed(request);
    }
}
