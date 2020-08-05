package com.proxy.service.api.cache;

import com.proxy.service.api.base.CloudNetWorkInterceptor;

import java.util.ArrayList;

/**
 * 拦截器缓存
 *
 * @author : cangHX
 * on 2020/08/04  9:37 PM
 */
public class InterceptorCache {

    private static final ArrayList<CloudNetWorkInterceptor> INTERCEPTORS_MAPPER = new ArrayList<>();

    public static ArrayList<CloudNetWorkInterceptor> getInterceptors() {
        return new ArrayList<>(INTERCEPTORS_MAPPER);
    }

    public static void putInterceptor(CloudNetWorkInterceptor interceptor) {
        if (interceptor == null) {
            return;
        }
        INTERCEPTORS_MAPPER.add(interceptor);
    }
}
