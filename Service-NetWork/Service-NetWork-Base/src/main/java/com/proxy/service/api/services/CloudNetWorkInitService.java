package com.proxy.service.api.services;

import com.proxy.service.api.base.CloudNetWorkCache;
import com.proxy.service.api.base.CloudNetWorkCookieJar;
import com.proxy.service.api.base.CloudNetWorkInterceptor;
import com.proxy.service.api.base.CloudNetWorkMock;
import com.proxy.service.api.base.CloudNetWorkProxy;
import com.proxy.service.api.base.CloudNetWorkSslSocket;
import com.proxy.service.base.BaseService;

import java.util.concurrent.TimeUnit;

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
    CloudNetWorkInitService setBaseUrl(String baseUrl);

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
    CloudNetWorkInitService setRequestTimeout(long timeout, TimeUnit unit);

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
    CloudNetWorkInitService setReadTimeout(long timeout, TimeUnit unit);

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
    CloudNetWorkInitService setWriteTimeout(long timeout, TimeUnit unit);

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
    CloudNetWorkInitService setConnectTimeout(long timeout, TimeUnit unit);

    /**
     * 添加拦截器
     *
     * @param interceptor : 拦截器对象
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:04 PM
     */
    CloudNetWorkInitService addInterceptor(CloudNetWorkInterceptor interceptor);

    /**
     * 设置代理
     *
     * @param proxy : 网络代理对象
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:07 PM
     */
    CloudNetWorkInitService setProxy(CloudNetWorkProxy proxy);

    /**
     * 设置 cookie
     *
     * @param cookieJar : 网络 cookie
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:09 PM
     */
    CloudNetWorkInitService setCookieJar(CloudNetWorkCookieJar cookieJar);

    /**
     * 设置网络缓存
     *
     * @param cache : 网络缓存
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:12 PM
     */
    CloudNetWorkInitService setCache(CloudNetWorkCache cache);

    /**
     * 设置网络模拟
     *
     * @param mock : 网络模拟
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:14 PM
     */
    CloudNetWorkInitService setMock(CloudNetWorkMock mock);

    /**
     * 设置 https 安全套接层
     *
     * @param sslSocket : 安全套接层
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:17 PM
     */
    CloudNetWorkInitService setSslSocket(CloudNetWorkSslSocket sslSocket);

    /**
     * 设置重试次数，请求失败自动重试，默认不重试
     *
     * @param count : 重试次数
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:23 PM
     */
    CloudNetWorkInitService setRetryCount(int count);
}
