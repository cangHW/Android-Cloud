package com.proxy.service.api.request.cache;

import com.proxy.service.api.request.base.CloudNetWorkInterceptor;

import java.util.ArrayList;

/**
 * @author : cangHX
 * on 2020/09/02  9:13 PM
 */
public class NetWorkInterceptorCache {

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
