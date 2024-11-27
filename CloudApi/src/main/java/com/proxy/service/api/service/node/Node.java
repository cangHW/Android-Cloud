package com.proxy.service.api.service.node;

import androidx.annotation.NonNull;

import com.proxy.service.api.service.listener.Converter;
import com.proxy.service.base.BaseService;

/**
 * 自定义数据格式
 *
 * @author: cangHX
 * on 2020/05/22  17:23
 */
public final class Node {

    /**
     * 默认id
     */
    public static final String UUID_DEFAULT = "default";

    private final String uuid;

    private final Converter<? extends BaseService> converter;

    Node(@NonNull String uuid, @NonNull Converter<? extends BaseService> converter) {
        this.uuid = uuid;
        this.converter = converter;
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    @NonNull
    public Converter<? extends BaseService> getConverter() {
        return converter;
    }
}
