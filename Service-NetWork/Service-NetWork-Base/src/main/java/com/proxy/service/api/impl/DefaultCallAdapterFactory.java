package com.proxy.service.api.impl;

import com.proxy.service.api.callback.request.CloudNetWorkCallAdapter;
import com.proxy.service.api.callback.request.CloudNetWorkCall;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 默认适配器工厂
 *
 * @author : cangHX
 * on 2020/07/30  5:25 PM
 */
public class DefaultCallAdapterFactory extends CloudNetWorkCallAdapter.Factory {

    /**
     * 获取支持当前返回类型的 adapter
     *
     * @param returnType : 返回值类型
     * @return 支持的 adapter 对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:59 PM
     */
    @Override
    public CloudNetWorkCallAdapter<?, ?> get(Type returnType) {
        Class<?> tClass = getRawType(returnType);
        if (tClass == CloudNetWorkCall.class && returnType instanceof ParameterizedType) {
            Type type = getParameterUpperBound(0, (ParameterizedType) returnType);
            return new DefaultCallAdapter(type);
        }
        return null;
    }
}
