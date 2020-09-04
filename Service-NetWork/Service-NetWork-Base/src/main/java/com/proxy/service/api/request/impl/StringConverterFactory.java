package com.proxy.service.api.request.impl;

import com.proxy.service.api.callback.converter.CloudNetWorkConverter;

import java.lang.reflect.Type;

/**
 * @author : cangHX
 * on 2020/08/05  10:11 PM
 */
public class StringConverterFactory extends CloudNetWorkConverter.Factory {
    /**
     * response 转换器
     *
     * @param type : 数据类型
     * @return 支持的转换器
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 7:54 PM
     */
    @Override
    public CloudNetWorkConverter<?> responseBodyConverter(Type type) {
        Class<?> tClass = getRawType(type);
        if (tClass == String.class) {
            return new StringConverter();
        }
        return null;
    }
}
