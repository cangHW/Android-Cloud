package com.proxy.service.api.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

/**
 * @author : cangHX
 * on 2020/07/21  7:15 PM
 */
public class BaseUrlCache {

    private static String mBaseUrl;
    private static final HashMap<String, String> BASE_URL_MAPPER = new HashMap<>();

    public static void setBaseUrl(@NonNull String baseUrl) {
        mBaseUrl = baseUrl;
    }

    public static void setBaseUrl(@NonNull String id, @NonNull String url) {
        BASE_URL_MAPPER.put(id, url);
    }

    public static String getBaseUrl() {
        return mBaseUrl;
    }

    @Nullable
    public static String getBaseUrl(@NonNull String id) {
        return BASE_URL_MAPPER.get(id);
    }

}
