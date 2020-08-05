package com.proxy.service.network.retrofit;

import android.os.Build;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiNewInstance;
import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.cache.ConverterCache;
import com.proxy.service.api.callback.converter.CloudNetWorkConverter;
import com.proxy.service.api.callback.request.CloudNetWorkCallAdapter;
import com.proxy.service.api.callback.request.CloudNetWorkCall;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.impl.BodyNetWorkCall;
import com.proxy.service.api.method.ServiceMethod;
import com.proxy.service.api.cache.ServiceMethodCache;
import com.proxy.service.api.method.ServiceReturnHandler;
import com.proxy.service.api.services.CloudNetWorkRequestService;
import com.proxy.service.api.tag.CloudServiceTagNetWork;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.api.utils.ServiceUtils;
import com.proxy.service.network.cache.CallCache;
import com.proxy.service.network.cache.RequestInfo;
import com.proxy.service.network.factory.RequestManager;

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

    private int mRetryCount = -1;
    private boolean isHasCookie = true;

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
        this.mRetryCount = count;
        return this;
    }

    /**
     * 移除本次请求的 cookie
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/20 9:09 PM
     */
    @NonNull
    @Override
    public CloudNetWorkRequestService removeCookie() {
        this.isHasCookie = false;
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
    public <T> T create(@NonNull final String tag, @NonNull final Class<T> service) {
        final boolean isService = ServiceUtils.checkServiceInterface(service);
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new InvocationHandler() {
            private final Object[] objects = new Object[0];

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

                if (!isService) {
                    Logger.Error(CloudApiError.DATA_TYPE_ERROR.setMsg("You need to use the interface type here. with : " + service.getCanonicalName()).build());
                    return null;
                }
                ServiceMethod serviceMethod = ServiceMethodCache.loadServiceMethod(method);
                if (serviceMethod == null) {
                    Logger.Error(CloudApiError.UNKNOWN_ERROR.build());
                    return null;
                }
                if (serviceMethod.isRequestBodyError()) {
                    Logger.Error(CloudApiError.DATA_ERROR.setMsg("There has some error to check. with : " + service.getCanonicalName()).build());
                    return null;
                }
                ServiceReturnHandler returnHandler = serviceMethod.getServiceReturnHandler();
                if (returnHandler == null) {
                    Logger.Error(CloudApiError.DATA_EMPTY.setMsg("There needs to be a return object. with : " + service.getCanonicalName()).build());
                    return null;
                }
                CloudNetWorkCallAdapter<?, Object> callAdapter = (CloudNetWorkCallAdapter<?, Object>) returnHandler.getAdapter();
                if (callAdapter == null) {
                    Logger.Error(CloudApiError.DATA_TYPE_ERROR.setMsg("Return types are not supported. with : " + service.getCanonicalName()).build());
                    return null;
                }
                CloudNetWorkConverter<Object> converter = (CloudNetWorkConverter<Object>) ConverterCache.getConverter(callAdapter.responseType());
                int timeOut = RequestInfo.getRequestTimeout();
                int retryCount = RequestInfo.getRetryCount();
                if (mRetryCount >= 0) {
                    retryCount = mRetryCount;
                }
                BodyNetWorkCall<Object> bodyNetWorkCall = new BodyNetWorkCall<>(
                        timeOut,
                        serviceMethod.request(args != null ? args : objects),
                        RequestManager.builder()
                                .setHasCookie(isHasCookie)
                                .setRetryCount(retryCount)
                                .build(),
                        converter);
                Object object = callAdapter.adapt(bodyNetWorkCall);
                if (object instanceof CloudNetWorkCall) {
                    CallCache.put(tag, NetWorkRequestServiceImpl.this, (CloudNetWorkCall<?>) object);
                }
                return object;

                //todo
//                return RetrofitManager.INSTANCE.startRequest(isService, serviceMethod, NetWorkRequestServiceImpl.this, tag);
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
        CallCache.cancelByTag(tag);
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
        CallCache.cancelAll();
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
        CallCache.cancelByService(this);
    }
}
