package com.proxy.service.api.request.base;

import com.proxy.service.api.request.method.CloudNetWorkHttpUrl;

/**
 * 网络模拟
 *
 * @author : cangHX
 * on 2020/07/20  9:13 PM
 */
public interface CloudNetWorkMock {

    /**
     * 加载 mock 数据
     *
     * @param httpUrl : 本次请求的网络地址
     * @return mock 数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/1 10:01 PM
     */
    String loadMock(CloudNetWorkHttpUrl httpUrl);

}
