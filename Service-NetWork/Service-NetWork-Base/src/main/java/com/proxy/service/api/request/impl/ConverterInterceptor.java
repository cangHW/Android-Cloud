package com.proxy.service.api.request.impl;

import androidx.annotation.NonNull;

import com.proxy.service.api.request.base.CloudNetWorkInterceptor;
import com.proxy.service.api.callback.converter.CloudNetWorkConverter;
import com.proxy.service.api.callback.response.CloudNetWorkResponse;
import com.proxy.service.api.request.method.CloudNetWorkRequest;
import com.proxy.service.api.log.Logger;

/**
 * 转换拦截器
 *
 * @author : cangHX
 * on 2020/08/04  9:55 PM
 */
public class ConverterInterceptor implements CloudNetWorkInterceptor {

    private CloudNetWorkConverter<?> mConverter;

    public ConverterInterceptor(CloudNetWorkConverter<?> converter) {
        this.mConverter = converter;
    }

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
        CloudNetWorkResponse<T> response = chain.proceed(request);
        if (!response.isSuccessful() || mConverter == null) {
            return response;
        }
        try {
            String value = (String) response.response();
            T t = (T) mConverter.convert(value);
            return CloudNetWorkResponse.success(response.code(), t);
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        return CloudNetWorkResponse.error(new ClassCastException("type error"));
    }
}
