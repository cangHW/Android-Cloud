package com.proxy.service.network.retrofit;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.request.base.CloudNetWorkCache;
import com.proxy.service.api.request.base.CloudNetWorkCookieJar;
import com.proxy.service.api.request.base.CloudNetWorkInterceptor;
import com.proxy.service.api.request.base.CloudNetWorkMock;
import com.proxy.service.api.request.cache.BaseUrlCache;
import com.proxy.service.api.request.cache.CallFactoryCache;
import com.proxy.service.api.request.cache.CallbackManager;
import com.proxy.service.api.request.cache.ConverterCache;
import com.proxy.service.api.request.cache.CookieCache;
import com.proxy.service.api.request.cache.InterceptorCache;
import com.proxy.service.api.request.cache.MockCache;
import com.proxy.service.api.request.cache.NetWorkInterceptorCache;
import com.proxy.service.api.callback.converter.CloudNetWorkConverter;
import com.proxy.service.api.callback.request.CloudNetWorkCallAdapter;
import com.proxy.service.api.callback.request.CloudNetWorkGlobalCallback;
import com.proxy.service.api.services.CloudNetWorkInitService;
import com.proxy.service.api.tag.CloudServiceTagNetWork;
import com.proxy.service.network.cache.RequestInfo;
import com.proxy.service.network.factory.OkHttpFactory;
import com.proxy.service.network.conversion.CacheConversion;
import com.proxy.service.network.impl.GlobalRequestCallbackImpl;
import com.proxy.service.network.utils.TimeUtils;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @author : cangHX
 * on 2020/07/21  4:12 PM
 */
@CloudApiService(serviceTag = CloudServiceTagNetWork.NET_WORK_INIT)
public class NetWorkInitServiceImpl implements CloudNetWorkInitService {

    private OkHttpFactory.Builder mOkHttpBuilder = new OkHttpFactory.Builder();

    public NetWorkInitServiceImpl() {
        CallbackManager.setGlobalCallback(new GlobalRequestCallbackImpl());
    }

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
        BaseUrlCache.setBaseUrl(baseUrl);
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
        RequestInfo.setRequestTimeout(TimeUtils.toMillis("RequestTimeout", timeout, unit));
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
        RequestInfo.setReadTimeout(TimeUtils.toMillis("ReadTimeout", timeout, unit));
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
        RequestInfo.setWriteTimeout(TimeUtils.toMillis("WriteTimeout", timeout, unit));
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
        RequestInfo.setConnectTimeout(TimeUtils.toMillis("ConnectTimeout", timeout, unit));
        mOkHttpBuilder.setConnectTimeout(TimeUtils.toMillis("ConnectTimeout", timeout, unit));
        return this;
    }

    /**
     * 设置全局请求回调
     *
     * @param callback : 全局回调接口
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/5 10:55 PM
     */
    @NonNull
    @Override
    public CloudNetWorkInitService setGlobalRequestCallback(@NonNull CloudNetWorkGlobalCallback callback) {
        CallbackManager.setGlobalCallback(callback);
        return this;
    }

    /**
     * 设置自定义转换器
     *
     * @param factory : 转换器工厂对象
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/5 10:55 PM
     */
    @NonNull
    @Override
    public CloudNetWorkInitService addConverterFactory(CloudNetWorkConverter.Factory factory) {
        ConverterCache.addConverter(factory);
        return this;
    }

    /**
     * 设置回调接口适配器工厂
     *
     * @param factory : 回调接口适配器工厂对象
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/5 10:55 PM
     */
    @NonNull
    @Override
    public CloudNetWorkInitService addCallAdapterFactory(CloudNetWorkCallAdapter.Factory factory) {
        CallFactoryCache.addCallFactory(factory);
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
        InterceptorCache.putInterceptor(interceptor);
        return this;
    }

    /**
     * 添加网络拦截器，生效于真实请求之前与真实请求之后
     *
     * @param interceptor : 拦截器对象
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:04 PM
     */
    @NonNull
    @Override
    public CloudNetWorkInitService addNetWorkInterceptor(@NonNull CloudNetWorkInterceptor interceptor) {
        NetWorkInterceptorCache.putInterceptor(interceptor);
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
    public CloudNetWorkInitService setProxy(@NonNull Proxy proxy) {
        mOkHttpBuilder.setProxy(proxy);
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
        CookieCache.putCookieJar(cookieJar);
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
        mOkHttpBuilder.setCache(CacheConversion.create(cache));
        return this;
    }

    /**
     * 设置 mock 数据
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
        MockCache.putMock(mock);
        return this;
    }

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
    @Override
    public CloudNetWorkInitService setSslSocket(@NonNull SSLSocketFactory sslSocket, @NonNull X509TrustManager manager) {
        mOkHttpBuilder.setSslSocketFactory(sslSocket);
        mOkHttpBuilder.setX509TrustManager(manager);
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
        RequestInfo.setRetryCount(count);
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
        OkHttpFactory.getInstance().setBuilder(mOkHttpBuilder);
    }
}
