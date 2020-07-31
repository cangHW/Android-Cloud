package com.proxy.service.network.retrofit;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.annotations.CloudApiNewInstance;
import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.base.CloudNetWorkCookieJar;
import com.proxy.service.api.callback.request.CallAdapter;
import com.proxy.service.api.callback.request.CloudNetWorkCall;
import com.proxy.service.api.callback.request.NetWorkCallback;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.impl.BodyNetWorkCall;
import com.proxy.service.api.impl.CloudNetWorkCookieJarEmpty;
import com.proxy.service.api.method.ServiceMethod;
import com.proxy.service.api.method.ServiceMethodCache;
import com.proxy.service.api.method.ServiceReturnHandler;
import com.proxy.service.api.services.CloudNetWorkRequestService;
import com.proxy.service.api.tag.CloudServiceTagNetWork;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.api.utils.ServiceUtils;
import com.proxy.service.network.factory.RequestManager;
import com.proxy.service.network.factory.RetrofitManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

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
    public <T> T create(@NonNull final String tag, @NonNull final Class<T> service) {
        final boolean isService = ServiceUtils.checkServiceInterface(service);
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
                CallAdapter<?, Object> callAdapter = (CallAdapter<?, Object>) returnHandler.getAdapter();
                if (callAdapter == null) {
                    Logger.Error(CloudApiError.DATA_TYPE_ERROR.setMsg("Return types are not supported. with : " + service.getCanonicalName()).build());
                    return null;
                }
                BodyNetWorkCall<Object> bodyNetWorkCall = new BodyNetWorkCall<>(serviceMethod.request(args), RequestManager.builder().build());
                return callAdapter.adapt(bodyNetWorkCall);

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
