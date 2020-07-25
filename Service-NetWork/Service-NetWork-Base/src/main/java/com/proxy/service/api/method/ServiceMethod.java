package com.proxy.service.api.method;

import androidx.annotation.Nullable;

import java.lang.reflect.Method;

/**
 * @author : cangHX
 * on 2020/7/24 8:57 PM
 */
public class ServiceMethod<T> {


    public T invoke(@Nullable Object[] args) {


        return null;
    }


    public static ServiceMethod<?> parseAnnotations(Method method) {


        return new ServiceMethod<>();
    }
}
