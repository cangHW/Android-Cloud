package com.proxy.service.api.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.base.CloudNetWorkCookieJar;
import com.proxy.service.base.BaseService;

/**
 * 网络模块请求服务
 *
 * @author : cangHX
 * on 2020/07/19  5:33 PM
 */
public interface CloudNetWorkRequestService extends BaseService {

    /**
     * 设置本次请求重试次数
     *
     * @param count : 重试次数
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:23 PM
     */
    @NonNull
    CloudNetWorkRequestService setRetryCount(int count);

    /**
     * 设置本次请求 cookie，传入 null 或者 {@link com.proxy.service.api.impl.CloudNetWorkCookieJarEmpty}，则取消本次请求的cookie
     *
     * @param cookieJar : 网络 cookie
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:09 PM
     */
    @NonNull
    CloudNetWorkRequestService setCookieJar(@Nullable CloudNetWorkCookieJar cookieJar);

    /**
     * 创建网络请求
     *
     * @param service : 请求接口类 class 对象
     * @return 接口类对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 8:57 PM
     */
    @NonNull
    <T> T create(@NonNull Class<T> service);

    /**
     * 创建网络请求，并绑定 tag
     *
     * @param tag : 身份信息，用于标示本次请求，一对多，一个 tag 可以绑定多个请求
     * @param service   : 请求接口类 class 对象
     * @return 接口类对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 8:57 PM
     */
    @NonNull
    <T> T create(@NonNull String tag, @NonNull Class<T> service);

    /**
     * 通过 tag 取消请求
     *
     * @param tag : 身份信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 8:57 PM
     */
    void cancelByTag(@NonNull String tag);

    /**
     * 取消所有请求
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 8:57 PM
     */
    void cancelAllOfApp();

    /**
     * 取消通过当前 service 创建的请求
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 8:57 PM
     */
    void cancelAllOfService();
}
