package com.proxy.service.api.impl;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.proxy.service.api.callback.converter.CloudNetWorkConverter;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author : cangHX
 * on 2020/08/05  10:22 PM
 */
public class GsonConverter<T> implements CloudNetWorkConverter<T> {

    private Gson mGson;
    private TypeAdapter<T> mAdapter;

    public GsonConverter(Gson gson, TypeAdapter<T> adapter) {
        this.mGson = gson;
        this.mAdapter = adapter;
    }

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
    public T convert(@Nullable String value) throws IOException {
        T response;
        if (value == null) {
            return null;
        }
        JsonReader jsonReader = this.mGson.newJsonReader(new StringReader(value));
        response = this.mAdapter.read(jsonReader);
        if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
            throw new JsonIOException("JSON document was not fully consumed.");
        }
        return response;
    }
}
