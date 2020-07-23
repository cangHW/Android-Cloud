package com.proxy.service.api.services;

import androidx.annotation.NonNull;

import com.proxy.service.api.base.CloudNetWorkCache;
import com.proxy.service.api.base.CloudNetWorkCookieJar;
import com.proxy.service.api.base.CloudNetWorkInterceptor;
import com.proxy.service.api.base.CloudNetWorkMock;
import com.proxy.service.base.BaseService;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 网络模块初始化服务
 *
 * @author: cangHX
 * on 2020/06/05  15:35
 */
public interface CloudNetWorkInitService extends BaseService {

    /**
     * 设置 BaseUrl
     *
     * @param baseUrl : baseUrl
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 8:57 PM
     */
    @NonNull
    CloudNetWorkInitService setBaseUrl(@NonNull String baseUrl);

    /**
     * 设置多个 BaseUrl，方便进行 baseUrl 的动态替换
     *
     * @param id      : baseUrl 对应的 Id
     * @param baseUrl : baseUrl
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 8:57 PM
     */
    @NonNull
    CloudNetWorkInitService setBaseUrls(@NonNull String id, @NonNull String baseUrl);

    /**
     * 设置请求超时，包括重试时间
     *
     * @param timeout : 超时时间
     * @param unit    : 时间粒度
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:25 PM
     */
    @NonNull
    CloudNetWorkInitService setRequestTimeout(long timeout, @NonNull TimeUnit unit);

    /**
     * 设置读取超时
     *
     * @param timeout : 超时时间
     * @param unit    : 时间粒度
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:01 PM
     */
    @NonNull
    CloudNetWorkInitService setReadTimeout(long timeout, @NonNull TimeUnit unit);

    /**
     * 设置写入超时
     *
     * @param timeout : 超时时间
     * @param unit    : 时间粒度
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:01 PM
     */
    @NonNull
    CloudNetWorkInitService setWriteTimeout(long timeout, @NonNull TimeUnit unit);

    /**
     * 设置连接超时
     *
     * @param timeout : 超时时间
     * @param unit    : 时间粒度
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:01 PM
     */
    @NonNull
    CloudNetWorkInitService setConnectTimeout(long timeout, @NonNull TimeUnit unit);

    /**
     * 添加拦截器
     *
     * @param interceptor : 拦截器对象
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:04 PM
     */
    @NonNull
    CloudNetWorkInitService addInterceptor(@NonNull CloudNetWorkInterceptor interceptor);

    /**
     * 设置代理
     *
     * @param proxy : 网络代理对象
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:07 PM
     */
    @NonNull
    CloudNetWorkInitService setProxy(@NonNull Proxy proxy);

    /**
     * 设置 cookie
     *
     * @param cookieJar : 网络 cookie
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:09 PM
     */
    @NonNull
    CloudNetWorkInitService setCookieJar(@NonNull CloudNetWorkCookieJar cookieJar);

    /**
     * 设置网络缓存
     *
     * @param cache : 网络缓存
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:12 PM
     */
    @NonNull
    CloudNetWorkInitService setCache(@NonNull CloudNetWorkCache cache);

    /**
     * 设置网络模拟
     *
     * @param mock : 网络模拟
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:14 PM
     */
    @NonNull
    CloudNetWorkInitService setMock(@NonNull CloudNetWorkMock mock);

    /**
     * 设置 https 安全套接层
     *
     * @param sslSocket : 安全套接层
     * @param manager   : 信任证书
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:17 PM
     */
    @NonNull
    CloudNetWorkInitService setSslSocket(@NonNull SSLSocketFactory sslSocket, @NonNull X509TrustManager manager);

    /**
     * 设置重试次数，请求失败自动重试，默认不重试
     *
     * @param count : 重试次数
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:23 PM
     */
    @NonNull
    CloudNetWorkInitService setRetryCount(int count);

    /**
     * 构建网络模块
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:23 PM
     */
    void build();
}
