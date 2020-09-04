package com.proxy.service.api.request.base;

import com.proxy.service.api.callback.response.CloudNetWorkCookie;
import com.proxy.service.api.request.method.CloudNetWorkHttpUrl;

import java.util.List;

/**
 * 网络 cookie
 *
 * @author : cangHX
 * on 2020/07/20  9:10 PM
 */
public interface CloudNetWorkCookieJar {

    /**
     * 从请求结果中保存 cookie
     *
     * @param url     : 对应请求地址
     * @param cookies : cookie 列表
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/1 9:44 PM
     */
    void saveFromResponse(CloudNetWorkHttpUrl url, List<CloudNetWorkCookie> cookies);

    /**
     * 为请求添加 cookie
     *
     * @param url : 本次请求的 url
     * @return 准备添加的 cookie 列表
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/1 9:45 PM
     */
    List<CloudNetWorkCookie> loadForRequest(CloudNetWorkHttpUrl url);
}
