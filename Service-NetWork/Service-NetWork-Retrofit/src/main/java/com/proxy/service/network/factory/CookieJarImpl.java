package com.proxy.service.network.factory;

import androidx.annotation.NonNull;

import com.proxy.service.api.request.base.CloudNetWorkCookieJar;
import com.proxy.service.api.request.cache.CookieCache;
import com.proxy.service.api.callback.response.CloudNetWorkCookie;
import com.proxy.service.api.request.method.CloudNetWorkHttpUrl;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @author : cangHX
 * on 2020/09/01  9:58 AM
 */
public class CookieJarImpl implements CookieJar {
    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        CloudNetWorkHttpUrl httpUrl = new CloudNetWorkHttpUrl.Builder().url(url.toString()).build();
        List<CloudNetWorkCookie> cookieList = new ArrayList<>();
        for (Cookie cookie : cookies) {
            CloudNetWorkCookie requestCookie = new CloudNetWorkCookie();
            requestCookie.key = cookie.name();
            requestCookie.value = cookie.value();
            cookieList.add(requestCookie);
        }
        for (CloudNetWorkCookieJar cookieJar : CookieCache.getCookieJars()) {
            cookieJar.saveFromResponse(httpUrl, cookieList);
        }
    }

    @NonNull
    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        return new ArrayList<>();
    }
}
