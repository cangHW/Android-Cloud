package com.proxy.service.api.cache;

import androidx.annotation.Nullable;

import com.proxy.service.api.callback.converter.CloudNetWorkConverter;
import com.proxy.service.api.impl.GsonConverterFactory;
import com.proxy.service.api.impl.StringConverterFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 转换器缓存
 *
 * @author : cangHX
 * on 2020/08/03  5:57 PM
 */
public class ConverterCache {

    private static final ArrayList<CloudNetWorkConverter.Factory> CONVERTER_MAPPER = new ArrayList<>();

    static {
        CONVERTER_MAPPER.add(new StringConverterFactory());
        CONVERTER_MAPPER.add(new GsonConverterFactory());
    }

    @Nullable
    public static CloudNetWorkConverter<?> getConverter(Type type) {
        for (CloudNetWorkConverter.Factory factory : CONVERTER_MAPPER) {
            CloudNetWorkConverter<?> converter = factory.responseBodyConverter(type);
            if (converter != null) {
                return converter;
            }
        }
        return null;
    }

    public static void addConverter(CloudNetWorkConverter.Factory factory) {
        if (factory == null) {
            return;
        }
        CONVERTER_MAPPER.add(0, factory);
    }
}
