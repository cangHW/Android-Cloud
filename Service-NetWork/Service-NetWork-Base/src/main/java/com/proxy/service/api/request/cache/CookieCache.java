package com.proxy.service.api.request.cache;

import com.proxy.service.api.request.base.CloudNetWorkCookieJar;
import com.proxy.service.api.request.impl.CloudNetWorkCookieJarEmpty;

import java.util.ArrayList;

/**
 * cookie 缓存
 *
 * @author : cangHX
 * on 2020/08/04  9:36 PM
 */
public class CookieCache {

    private static final ArrayList<CloudNetWorkCookieJar> COOKIE_MAPPER = new ArrayList<>();

    static {
        COOKIE_MAPPER.add(CloudNetWorkCookieJarEmpty.getInstance());
    }

    public static ArrayList<CloudNetWorkCookieJar> getCookieJars() {
        return new ArrayList<>(COOKIE_MAPPER);
    }

    public static void putCookieJar(CloudNetWorkCookieJar cookieJar) {
        if (cookieJar == null) {
            return;
        }
        COOKIE_MAPPER.add(cookieJar);
    }

}
