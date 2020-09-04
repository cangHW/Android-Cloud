package com.proxy.service.api.request.impl;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.proxy.service.api.request.base.CloudNetWorkInterceptor;
import com.proxy.service.api.request.base.CloudNetWorkMock;
import com.proxy.service.api.request.cache.MockCache;
import com.proxy.service.api.callback.response.CloudNetWorkResponse;
import com.proxy.service.api.request.method.CloudNetWorkHttpUrl;
import com.proxy.service.api.request.method.CloudNetWorkRequest;

import java.util.ArrayList;

/**
 * mock 数据
 *
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
    @SuppressWarnings("unchecked")
    @Override
    public <T> CloudNetWorkResponse<T> intercept(@NonNull Chain chain) {
        CloudNetWorkRequest request = chain.request();
        CloudNetWorkHttpUrl httpUrl = request.httpUrl();
        ArrayList<CloudNetWorkMock> mocks = MockCache.getMocks();
        for (CloudNetWorkMock mock : mocks) {
            String json = mock.loadMock(httpUrl);
            if (!TextUtils.isEmpty(json)) {
                return (CloudNetWorkResponse<T>) CloudNetWorkResponse.success(json);
            }
        }
        return chain.proceed(request);
    }
}
