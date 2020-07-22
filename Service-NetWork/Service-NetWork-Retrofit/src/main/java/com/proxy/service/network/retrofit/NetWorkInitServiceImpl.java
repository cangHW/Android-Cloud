package com.proxy.service.network.retrofit;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.base.CloudNetWorkCache;
import com.proxy.service.api.base.CloudNetWorkCookieJar;
import com.proxy.service.api.base.CloudNetWorkInterceptor;
import com.proxy.service.api.base.CloudNetWorkMock;
import com.proxy.service.api.base.CloudNetWorkProxy;
import com.proxy.service.api.base.CloudNetWorkSslSocket;
import com.proxy.service.api.cache.BaseUrlCache;
import com.proxy.service.api.services.CloudNetWorkInitService;
import com.proxy.service.api.tag.CloudServiceTagNetWork;
import com.proxy.service.network.cache.RequestInfo;
import com.proxy.service.network.factory.OkHttpFactory;
import com.proxy.service.network.factory.RetrofitFactory;
import com.proxy.service.network.interceptors.DefaultInterceptor;
import com.proxy.service.network.utils.TimeUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author : cangHX
 * on 2020/07/21  4:12 PM
 */
@CloudApiService(serviceTag = CloudServiceTagNetWork.NET_WORK_INIT)
public class NetWorkInitServiceImpl implements CloudNetWorkInitService {

    private RetrofitFactory.Builder mRetrofitBuilder = new RetrofitFactory.Builder();
    private OkHttpFactory.Builder mOkHttpBuilder = new OkHttpFactory.Builder();

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
    @Override
    public CloudNetWorkInitService setBaseUrl(@NonNull String baseUrl) {
        mRetrofitBuilder.setBaseUrl(baseUrl);
        return this;
    }

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
    @Override
    public CloudNetWorkInitService setBaseUrls(@NonNull String id, @NonNull String baseUrl) {
        BaseUrlCache.setBaseUrl(id, baseUrl);
        return this;
    }

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
    @Override
    public CloudNetWorkInitService setRequestTimeout(long timeout, @NonNull TimeUnit unit) {
        RequestInfo.INSTANCE.setRequestTimeout(TimeUtils.toMillis("RequestTimeout", timeout, unit));
        return this;
    }

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
    @Override
    public CloudNetWorkInitService setReadTimeout(long timeout, @NonNull TimeUnit unit) {
        mOkHttpBuilder.setReadTimeout(TimeUtils.toMillis("ReadTimeout", timeout, unit));
        return this;
    }

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
    @Override
    public CloudNetWorkInitService setWriteTimeout(long timeout, @NonNull TimeUnit unit) {
        mOkHttpBuilder.setWriteTimeout(TimeUtils.toMillis("WriteTimeout", timeout, unit));
        return this;
    }

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
    @Override
    public CloudNetWorkInitService setConnectTimeout(long timeout, @NonNull TimeUnit unit) {
        mOkHttpBuilder.setConnectTimeout(TimeUtils.toMillis("ConnectTimeout", timeout, unit));
        return this;
    }

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
    @Override
    public CloudNetWorkInitService addInterceptor(@NonNull CloudNetWorkInterceptor interceptor) {
        mOkHttpBuilder.addInterceptor(new DefaultInterceptor(interceptor));
        return this;
    }

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
    @Override
    public CloudNetWorkInitService setProxy(@NonNull CloudNetWorkProxy proxy) {
        //todo
        return this;
    }

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
    @Override
    public CloudNetWorkInitService setCookieJar(@NonNull CloudNetWorkCookieJar cookieJar) {
        //todo
        return this;
    }

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
    @Override
    public CloudNetWorkInitService setCache(@NonNull CloudNetWorkCache cache) {
        //todo
        return this;
    }

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
    @Override
    public CloudNetWorkInitService setMock(@NonNull CloudNetWorkMock mock) {
        //todo
        return this;
    }

    /**
     * 设置 https 安全套接层
     *
     * @param sslSocket : 安全套接层
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:17 PM
     */
    @NonNull
    @Override
    public CloudNetWorkInitService setSslSocket(@NonNull CloudNetWorkSslSocket sslSocket) {
        //todo
        return this;
    }

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
    @Override
    public CloudNetWorkInitService setRetryCount(int count) {
        RequestInfo.INSTANCE.setRetryCount(count);
        return this;
    }

    /**
     * 构建网络模块
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:23 PM
     */
    @Override
    public void build() {
        RetrofitFactory.INSTANCE.setBuilder(mRetrofitBuilder);
        OkHttpFactory.INSTANCE.setBuilder(mOkHttpBuilder);
    }
}
