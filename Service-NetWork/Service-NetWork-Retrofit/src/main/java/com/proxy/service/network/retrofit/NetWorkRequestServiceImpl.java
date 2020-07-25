package com.proxy.service.network.retrofit;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.annotations.CloudApiNewInstance;
import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.base.CloudNetWorkCookieJar;
import com.proxy.service.api.impl.CloudNetWorkCookieJarEmpty;
import com.proxy.service.api.method.ServiceMethodCache;
import com.proxy.service.api.services.CloudNetWorkRequestService;
import com.proxy.service.api.tag.CloudServiceTagNetWork;
import com.proxy.service.api.utils.ServiceUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author : cangHX
 * on 2020/7/23 9:23 PM
 */
@CloudApiNewInstance
@CloudApiService(serviceTag = CloudServiceTagNetWork.NET_WORK_REQUEST)
public class NetWorkRequestServiceImpl implements CloudNetWorkRequestService {
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
    @Override
    public CloudNetWorkRequestService setRetryCount(int count) {
        return this;
    }

    /**
     * 设置本次请求 cookie，传入 null 或者 {@link CloudNetWorkCookieJarEmpty}，则取消本次请求的cookie
     *
     * @param cookieJar : 网络 cookie
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:09 PM
     */
    @NonNull
    @Override
    public CloudNetWorkRequestService setCookieJar(@Nullable CloudNetWorkCookieJar cookieJar) {
        return this;
    }

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
    @Override
    public <T> T create(@NonNull Class<T> service) {
        return create("", service);
    }

    /**
     * 创建网络请求，并绑定 tag
     *
     * @param tag     : 身份信息，用于标示本次请求，一对多，一个 tag 可以绑定多个请求
     * @param service : 请求接口类 class 对象
     * @return 接口类对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 8:57 PM
     */
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T> T create(@NonNull String tag, @NonNull Class<T> service) {
        boolean flag = ServiceUtils.checkServiceInterface(service);
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, args);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (method.isDefault()) {
                        return method.invoke(this, args);
                    }
                }
                return ServiceMethodCache.loadServiceMethod(method).invoke(args);
            }
        });
    }

    /**
     * 通过 tag 取消请求
     *
     * @param tag : 身份信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 8:57 PM
     */
    @Override
    public void cancelByTag(@NonNull String tag) {

    }

    /**
     * 取消所有请求
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 8:57 PM
     */
    @Override
    public void cancelAllOfApp() {

    }

    /**
     * 取消通过当前 service 创建的请求
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 8:57 PM
     */
    @Override
    public void cancelAllOfService() {

    }
}
