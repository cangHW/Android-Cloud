package com.proxy.service.api.request.impl;

import androidx.annotation.Nullable;

import com.proxy.service.api.callback.converter.CloudNetWorkConverter;

import java.io.IOException;

/**
 * @author : cangHX
 * on 2020/08/05  10:20 PM
 */
public class StringConverter implements CloudNetWorkConverter<String> {
    /**
     * 数据转换
     *
     * @param value : 待转换数据
     * @return 转换后的数据
     * @throws IOException 转换过程中的异常
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 7:36 PM
     */
    @Nullable
    @Override
    public String convert(@Nullable String value) throws IOException {
        return value;
    }
}
