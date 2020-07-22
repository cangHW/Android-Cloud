package com.proxy.service.api.impl;

import com.proxy.service.api.base.CloudNetWorkCookieJar;

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

    public CloudNetWorkCookieJar getInstance() {
        return Factory.INSTANCE;
    }

}
