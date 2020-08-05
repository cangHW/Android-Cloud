package com.proxy.service.api.impl;

import androidx.annotation.NonNull;

import com.proxy.service.api.base.CloudNetWorkInterceptor;
import com.proxy.service.api.cache.CallbackManager;
import com.proxy.service.api.cache.InterceptorCache;
import com.proxy.service.api.callback.request.CloudNetWorkCallback;
import com.proxy.service.api.callback.request.CloudNetWorkCall;
import com.proxy.service.api.callback.converter.CloudNetWorkConverter;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.method.CloudNetWorkRequest;
import com.proxy.service.api.callback.request.NetWorkCallback;
import com.proxy.service.api.callback.response.CloudNetWorkResponse;

import java.util.ArrayList;

/**
 * @author : cangHX
 * on 2020/07/30  10:23 PM
 */
public class BodyNetWorkCall<T> implements CloudNetWorkCall<T> {

    /**
     * 就绪
     */
    private static final int STATUS_DEFAULT = 0;
    /**
     * 执行
     */
    private static final int STATUS_EXECUTED = 1;
    /**
     * 取消
     */
    private static final int STATUS_CANCEL = 2;

    private int mTimeoutMillis;
    private CloudNetWorkRequest mRequest;
    private NetWorkCallback mCallback;
    private CloudNetWorkConverter<T> mConverter;
    private int mRequestStatus = STATUS_DEFAULT;

    public BodyNetWorkCall(int timeoutMillis, CloudNetWorkRequest request, NetWorkCallback callback, CloudNetWorkConverter<T> converter) {
        this.mTimeoutMillis = timeoutMillis;
        this.mRequest = request;
        this.mCallback = callback;
        this.mConverter = converter;
    }

    /**
     * 同步请求
     *
     * @return 请求结果
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/31 10:29 PM
     */
    @NonNull
    @Override
    public CloudNetWorkResponse<T> execute() {
        if (mRequestStatus != STATUS_DEFAULT) {
            //只允许执行一次
            return CloudNetWorkResponse.error(new IllegalAccessException("It can only run once"));
        }
        mRequestStatus = STATUS_EXECUTED;
        CallbackManager.start(this);
        if (mRequest == null || mCallback == null) {
            CloudNetWorkResponse<T> response = CloudNetWorkResponse.error(new IllegalAccessException(CloudApiError.UNKNOWN_ERROR.build()));
            CallbackManager.response(this, response, null);
            mRequestStatus = STATUS_CANCEL;
            CallbackManager.finish(this);
            return response;
        }
        ArrayList<CloudNetWorkInterceptor> arrayList = InterceptorCache.getInterceptors();
        arrayList.add(new ConverterInterceptor(mConverter));
        arrayList.add(new MockInterceptor());
        arrayList.add(new RealRequestInterceptor(true, mCallback, mTimeoutMillis));

        RequestChain requestChain = new RequestChain(mRequest, this, arrayList, 0);
        CloudNetWorkResponse<T> response = null;
        try {
            response = requestChain.proceed(mRequest);
        } catch (Throwable ignored) {
        }

        if (response == null) {
            response = CloudNetWorkResponse.error(new NullPointerException("no data"));
        }
        CallbackManager.response(this, response, null);
        mRequestStatus = STATUS_CANCEL;
        CallbackManager.finish(this);
        return response;
    }

    /**
     * 异步请求
     *
     * @param callback : 请求回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/31 10:30 PM
     */
    @Override
    public void enqueue(@NonNull final CloudNetWorkCallback<T> callback) {
        if (mRequestStatus != STATUS_DEFAULT) {
            //只允许执行一次
            callback.onFailure(new IllegalAccessException("It can only run once"));
            return;
        }
        mRequestStatus = STATUS_EXECUTED;
        CallbackManager.start(this);
        if (mRequest == null || mCallback == null) {
            mRequestStatus = STATUS_CANCEL;
            CallbackManager.failure(this, new IllegalAccessException(CloudApiError.UNKNOWN_ERROR.build()), callback);
            CallbackManager.finish(this);
            return;
        }
        //todo 线程切换，子线程
        ArrayList<CloudNetWorkInterceptor> arrayList = InterceptorCache.getInterceptors();
        arrayList.add(new ConverterInterceptor(mConverter));
        arrayList.add(new MockInterceptor());
        arrayList.add(new RealRequestInterceptor(false, mCallback, mTimeoutMillis));

        RequestChain requestChain = new RequestChain(mRequest, this, arrayList, 0);
        CloudNetWorkResponse<T> response = null;
        try {
            response = requestChain.proceed(mRequest);
        } catch (Throwable ignored) {
        }
        if (mRequestStatus != STATUS_EXECUTED) {
            return;
        }
        if (response == null) {
            response = CloudNetWorkResponse.error(new NullPointerException("no data"));
        }
        if (response.isSuccessful()) {
            //todo 线程切换，主线程
//            callback.onResponse(response);
            CallbackManager.response(this, response, callback);
        } else {
            //todo 线程切换，主线程
//            callback.onFailure(response.throwable());
            CallbackManager.failure(this, response.throwable(), callback);
        }
        mRequestStatus = STATUS_CANCEL;
        CallbackManager.finish(this);
    }

    /**
     * 是否开始执行
     *
     * @return true 已经开始执行，false 未执行
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/31 10:31 PM
     */
    @Override
    public boolean isExecuted() {
        return mRequestStatus == STATUS_EXECUTED;
    }

    /**
     * 取消请求
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/31 10:32 PM
     */
    @Override
    public void cancel() {
        if (mRequestStatus == STATUS_EXECUTED) {
            mCallback.cancel();
        }
        mRequestStatus = STATUS_CANCEL;
    }

    /**
     * 请求是否已经取消
     *
     * @return true 已取消，false 未取消
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/31 10:32 PM
     */
    @Override
    public boolean isCanceled() {
        return mRequestStatus == STATUS_CANCEL;
    }

    /**
     * 获取请求体
     *
     * @return 请求体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/31 10:36 PM
     */
    @NonNull
    @Override
    public CloudNetWorkRequest request() {
        return mRequest;
    }
}
