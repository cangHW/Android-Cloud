package com.proxy.service.api.impl;

import androidx.annotation.NonNull;

import com.proxy.service.api.CloudSystem;
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
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.task.ITask;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.task.TaskCallableOnce;

import java.util.ArrayList;

/**
 * @author : cangHX
 * on 2020/07/30  10:23 PM
 */
public final class BodyNetWorkCall<T> implements CloudNetWorkCall<T> {

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

    private final CloudUtilsTaskService mTaskService;
    private final int mTimeoutMillis;
    private final CloudNetWorkRequest mRequest;
    private final NetWorkCallback mCallback;
    private final CloudNetWorkConverter<T> mConverter;
    private int mRequestStatus = STATUS_DEFAULT;

    public BodyNetWorkCall(int timeoutMillis, CloudNetWorkRequest request, NetWorkCallback callback, CloudNetWorkConverter<T> converter) {
        this.mTimeoutMillis = timeoutMillis;
        this.mRequest = request;
        this.mCallback = callback;
        this.mConverter = converter;
        this.mTaskService = CloudSystem.getService(CloudServiceTagUtils.UTILS_TASK);
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
        if (mTaskService == null) {
            CallbackManager.failure(BodyNetWorkCall.this, new IllegalAccessException(CloudApiError.UNKNOWN_ERROR.setMsg("Please check whether to use \"exclude\" to remove partial dependencies").build()), callback);
            return;
        }

        mTaskService.callUiThread(new Task<Boolean>() {
            @Override
            public Boolean call() {
                if (mRequestStatus != STATUS_DEFAULT) {
                    //只允许执行一次
                    CallbackManager.failure(BodyNetWorkCall.this, new IllegalAccessException("It can only run once"), callback);
                    return false;
                }
                mRequestStatus = STATUS_EXECUTED;
                if (mRequest == null || mCallback == null) {
                    mRequestStatus = STATUS_CANCEL;
                    CallbackManager.failure(BodyNetWorkCall.this, new IllegalAccessException(CloudApiError.UNKNOWN_ERROR.build()), callback);
                    return false;
                }
                CallbackManager.start(BodyNetWorkCall.this);
                return true;
            }
        }).workThread().call(new TaskCallableOnce<Boolean, CloudNetWorkResponse<T>>() {
            @Override
            public CloudNetWorkResponse<T> then(ITask<Boolean> iTask) {
                if (!iTask.isSuccess() || !iTask.getResponse()) {
                    return null;
                }

                ArrayList<CloudNetWorkInterceptor> arrayList = InterceptorCache.getInterceptors();
                arrayList.add(new ConverterInterceptor(mConverter));
                arrayList.add(new MockInterceptor());
                arrayList.add(new RealRequestInterceptor(false, mCallback, mTimeoutMillis));

                RequestChain requestChain = new RequestChain(mRequest, BodyNetWorkCall.this, arrayList, 0);
                CloudNetWorkResponse<T> response = null;
                try {
                    response = requestChain.proceed(mRequest);
                } catch (Throwable ignored) {
                }
                if (mRequestStatus != STATUS_EXECUTED) {
                    return null;
                }
                if (response == null) {
                    response = CloudNetWorkResponse.error(new NullPointerException("no data"));
                }
                return response;
            }
        }).mainThread().call(new TaskCallableOnce<CloudNetWorkResponse<T>, Object>() {
            @Override
            public Object then(ITask<CloudNetWorkResponse<T>> iTask) {
                CloudNetWorkResponse<T> response = iTask.getResponse();
                if (iTask.isSuccess() && response != null) {
                    if (response.isSuccessful()) {
                        CallbackManager.response(BodyNetWorkCall.this, response, callback);
                    } else {
                        CallbackManager.failure(BodyNetWorkCall.this, response.throwable(), callback);
                    }
                }
                mRequestStatus = STATUS_CANCEL;
                CallbackManager.finish(BodyNetWorkCall.this);
                return null;
            }
        });
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
        if (mTaskService != null) {
            mTaskService.cancel();
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
