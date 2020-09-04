package com.proxy.service.api.request.impl;

import com.proxy.service.api.request.base.CloudNetWorkCookieJar;
import com.proxy.service.api.callback.response.CloudNetWorkCookie;
import com.proxy.service.api.request.method.CloudNetWorkHttpUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认空 cookie
 *
 * @author : cangHX
 * on 2020/07/21  6:55 PM
 */
public class CloudNetWorkCookieJarEmpty implements CloudNetWorkCookieJar {

    private CloudNetWorkCookieJarEmpty() {
    }

    private static class Factory {
        private static final CloudNetWorkCookieJar INSTANCE = new CloudNetWorkCookieJarEmpty();
    }

    public static CloudNetWorkCookieJar getInstance() {
        return Factory.INSTANCE;
    }

    /**
     * 从请求结果中保存 cookie
     *
     * @param url     : 对应请求地址
     * @param cookies : cookie 列表
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/1 9:44 PM
     */
    @Override
    public void saveFromResponse(CloudNetWorkHttpUrl url, List<CloudNetWorkCookie> cookies) {

    }

    /**
     * 为请求添加 cookie
     *
     * @param url : 本次请求的 url
     * @return 准备添加的 cookie 列表
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/1 9:45 PM
     */
    @Override
    public List<CloudNetWorkCookie> loadForRequest(CloudNetWorkHttpUrl url) {
        return new ArrayList<>();
    }
}
