package com.proxy.service.api.request.method;

import com.proxy.service.api.request.cache.CallFactoryCache;
import com.proxy.service.api.callback.request.CloudNetWorkCallAdapter;

import java.lang.reflect.Type;

/**
 * @author : cangHX
 * on 2020/07/29  10:41 PM
 */
public class ServiceReturnHandler {

    private Type mType;

    public ServiceReturnHandler(Type type) {
        this.mType = type;
    }

    public CloudNetWorkCallAdapter<?, ?> getAdapter() {
        return CallFactoryCache.getAdapter(mType);
    }

}
