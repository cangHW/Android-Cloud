package com.cloud.api.manager.node;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cloud.api.manager.listener.Converter;

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

    private final Converter converter;
    private final WeakReference<Converter> weakReference;

    Node(@NonNull String uuid, @NonNull Converter converter) {
        this.uuid = uuid;
        if (UUID_DEFAULT.equals(uuid)) {
            weakReference = null;
            this.converter = converter;
        } else {
            this.weakReference = new WeakReference<>(converter);
            this.converter = null;
        }
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    @Nullable
    public Converter getConverter() {
        if (converter != null) {
            return converter;
        }
        return weakReference.get();
    }
}
