package com.proxy.service.api.request.impl;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.request.base.CloudNetWorkInterceptor;
import com.proxy.service.api.callback.request.NetWorkCallback;
import com.proxy.service.api.callback.request.RequestCallback;
import com.proxy.service.api.callback.response.CloudNetWorkResponse;
import com.proxy.service.api.request.method.CloudNetWorkRequest;
import com.proxy.service.api.utils.Logger;

import java.net.SocketTimeoutException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author : cangHX
 * on 2020/08/04  9:27 PM
 */
public class RealRequestInterceptor implements CloudNetWorkInterceptor {

    private boolean isExecute;
    private NetWorkCallback mCallback;
    private int mTimeoutMillis;

    public RealRequestInterceptor(boolean isExecute, NetWorkCallback callback, int timeoutMillis) {
        this.isExecute = isExecute;
        this.mCallback = callback;
        this.mTimeoutMillis = timeoutMillis;
    }

    /**
     * 拦截器执行体
     *
     * @param chain : 链条
     * @return 返回体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/4 10:18 PM
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> CloudNetWorkResponse<T> intercept(@NonNull Chain chain) {
        CloudNetWorkRequest request = chain.request();
        mCallback.setRequestBody(request);
        final CountDownLatch latch = new CountDownLatch(1);
        final CloudNetWorkResponse<T>[] responses = new CloudNetWorkResponse[]{null};
        if (isExecute) {
            String value = mCallback.execute();
            if (TextUtils.isEmpty(value)) {
                responses[0] = CloudNetWorkResponse.error(new NullPointerException("no data"));
            } else {
                responses[0] = (CloudNetWorkResponse<T>) CloudNetWorkResponse.success(value);
            }
            latch.countDown();
        } else {
            mCallback.enqueue(new RequestCallback() {
                @Override
                public void onResponse(int code, @Nullable String jsonString) {
                    if (TextUtils.isEmpty(jsonString)) {
                        responses[0] = CloudNetWorkResponse.error(code, new NullPointerException("no data"));
                    } else {
                        responses[0] = (CloudNetWorkResponse<T>) CloudNetWorkResponse.success(code, jsonString);
                    }
                    latch.countDown();
                }

                @Override
                public void onFailure(@Nullable Throwable t) {
                    if (t == null) {
                        responses[0] = CloudNetWorkResponse.error(new NullPointerException("no data"));
                    } else {
                        responses[0] = CloudNetWorkResponse.error(t);
                    }
                    latch.countDown();
                }
            });
        }
        try {
            if (mTimeoutMillis <= 0) {
                latch.await();
            } else {
                latch.await(mTimeoutMillis, TimeUnit.MILLISECONDS);
            }
        } catch (Throwable e) {
            Logger.Debug(e);
        }
        CloudNetWorkResponse<T> response = responses[0];
        if (response == null) {
            response = CloudNetWorkResponse.error(new SocketTimeoutException("time out"));
        }
        return response;
    }
}
