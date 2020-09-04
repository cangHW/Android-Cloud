package com.proxy.service.network.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.request.annotations.HttpMethod;
import com.proxy.service.api.request.base.CloudNetWorkCookieJar;
import com.proxy.service.api.request.cache.CookieCache;
import com.proxy.service.api.callback.response.CloudNetWorkCookie;
import com.proxy.service.api.request.method.CloudNetWorkRequest;
import com.proxy.service.network.factory.RetrofitManager;
import com.proxy.service.network.service.RetrofitService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * @author : cangHX
 * on 2020/08/27  10:03 PM
 */
public class RequestUtils {

    /**
     * 从请求体里面获取到 url
     *
     * @param request : 请求体
     * @return url
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/27 10:03 PM
     */
    @NonNull
    public static String getUrl(CloudNetWorkRequest request) {
        return request.httpUrl().url();
    }

    /**
     * 从请求体里面获取到参数的 map 集合
     *
     * @param request : 请求体
     * @return 参数的 map 集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/27 10:03 PM
     */
    @NonNull
    public static Map<String, String> getHeaders(CloudNetWorkRequest request, boolean isUseCookie) {
        Map<String, String> headers = request.headers().getHeadersMapper();
        if (headers == null) {
            headers = new HashMap<>(0);
        }

        if (!isUseCookie) {
            return headers;
        }

        for (CloudNetWorkCookieJar cookieJar : CookieCache.getCookieJars()) {
            List<CloudNetWorkCookie> cookies = cookieJar.loadForRequest(request.httpUrl());
            if (cookies == null || cookies.size() == 0) {
                continue;
            }
            for (CloudNetWorkCookie cookie : cookies) {
                if (cookie == null) {
                    continue;
                }
                String key = cookie.key;
                String value = cookie.value;
                if (TextUtils.isEmpty(key)) {
                    continue;
                }
                headers.put(key, value == null ? "" : value);
            }
        }
        return headers;
    }

    /**
     * 从请求体里面获取到参数的 map 集合
     *
     * @param request : 请求体
     * @return 参数的 map 集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/27 10:03 PM
     */
    @NonNull
    public static Map<String, String> getParams(CloudNetWorkRequest request) {
        Map<String, String> params = request.parameter().getParameterMap();
        if (params == null) {
            params = new HashMap<>(0);
        }
        return params;
    }

    /**
     * 从请求体里面获取到参数的 map 集合
     *
     * @param httpMethod : 请求方法
     * @param url        : 接口地址
     * @param headers    : header 集合
     * @param params     : 参数集合
     * @return 参数的 map 集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/27 10:03 PM
     */
    @Nullable
    public static Call<String> getCall(String httpMethod, String url, Map<String, String> headers, Map<String, String> params) {
        Call<String> call = null;
        if (httpMethod.equals(HttpMethod.GET)) {
            call = RetrofitManager.getInstance().getRetrofit().create(RetrofitService.class).get(url, headers, params);
        } else if (httpMethod.equals(HttpMethod.POST) && params.size() > 0) {
            call = RetrofitManager.getInstance().getRetrofit().create(RetrofitService.class).post(url, headers, params);
        } else if (httpMethod.equals(HttpMethod.POST)) {
            call = RetrofitManager.getInstance().getRetrofit().create(RetrofitService.class).post(url, headers);
        }
        return call;
    }

}
