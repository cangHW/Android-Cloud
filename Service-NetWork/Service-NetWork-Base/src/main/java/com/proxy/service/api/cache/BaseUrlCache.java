package com.proxy.service.api.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

/**
 * @author : cangHX
 * on 2020/07/21  5:15 PM
 */
public class BaseUrlCache {

    private static final HashMap<String, String> BASE_URL_MAPPER = new HashMap<>();

    static {
        //todo 数据本地恢复
    }

    public static void setBaseUrl(@NonNull String id, @NonNull String url) {
        BASE_URL_MAPPER.put(id, url);
    }

    @Nullable
    public static String getBaseUrl(@NonNull String id) {
        return BASE_URL_MAPPER.get(id);
    }

}
