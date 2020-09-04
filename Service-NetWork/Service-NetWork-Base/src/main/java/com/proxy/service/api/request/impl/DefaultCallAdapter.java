package com.proxy.service.api.request.impl;

import com.proxy.service.api.callback.request.CloudNetWorkCallAdapter;
import com.proxy.service.api.callback.request.CloudNetWorkCall;

import java.lang.reflect.Type;

/**
 * 默认适配器
 *
 * @author : cangHX
 * on 2020/07/30  5:24 PM
 */
public class DefaultCallAdapter implements CloudNetWorkCallAdapter<CloudNetWorkCall<?>, Object> {

    private Type mType;

    public DefaultCallAdapter(Type type) {
        this.mType = type;
    }

    @Override
    public Type responseType() {
        return mType;
    }

    @Override
    public CloudNetWorkCall<Object> adapt(CloudNetWorkCall<Object> call) {
        return call;
    }
}
