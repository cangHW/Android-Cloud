package com.proxy.service.api.service.node;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.service.listener.Converter;
import com.proxy.service.base.BaseService;

import java.lang.ref.WeakReference;

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
    private final WeakReference<Converter<? extends BaseService>> weakReference;

    Node(@NonNull String uuid, @NonNull Converter<? extends BaseService> converter) {
        this.uuid = uuid;
        if (UUID_DEFAULT.equals(uuid)) {
            weakReference = null;
            this.converter = converter;
        } else {
            this.weakReference = new WeakReference<Converter<? extends BaseService>>(converter);
            this.converter = null;
        }
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    @Nullable
    public Converter<? extends BaseService> getConverter() {
        if (converter != null) {
            return converter;
        }
        return weakReference.get();
    }
}
