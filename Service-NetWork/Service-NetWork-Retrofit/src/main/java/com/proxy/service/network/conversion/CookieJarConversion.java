package com.proxy.service.network.conversion;

import androidx.annotation.NonNull;

import com.proxy.service.api.base.CloudNetWorkCookieJar;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @author : cangHX
 * on 2020/7/23 9:11 PM
 */
public final class CookieJarConversion implements CookieJar {

    private CloudNetWorkCookieJar mCookieJar;

    private CookieJarConversion(CloudNetWorkCookieJar cookieJar) {
        this.mCookieJar = cookieJar;
    }

    public static CookieJarConversion create(CloudNetWorkCookieJar cookieJar){

        return new CookieJarConversion(cookieJar);
    }

    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {

    }

    @Override
    @NonNull
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        return null;
    }
}
