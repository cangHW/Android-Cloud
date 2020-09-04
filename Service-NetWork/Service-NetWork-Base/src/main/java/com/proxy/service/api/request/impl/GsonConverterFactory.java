package com.proxy.service.api.request.impl;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.proxy.service.api.callback.converter.CloudNetWorkConverter;

import java.lang.reflect.Type;

/**
 * @author : cangHX
 * on 2020/08/05  10:14 PM
 */
public class GsonConverterFactory extends CloudNetWorkConverter.Factory {

    private Gson mGson;

    public GsonConverterFactory() {
        mGson = new Gson();
    }

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
        TypeAdapter<?> adapter = this.mGson.getAdapter(TypeToken.get(type));
        return new GsonConverter<>(mGson, adapter);
    }
}
