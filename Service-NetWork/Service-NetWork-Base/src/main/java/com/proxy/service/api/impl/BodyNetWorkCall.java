package com.proxy.service.api.impl;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.request.CloudNetWorkCallback;
import com.proxy.service.api.callback.request.CloudNetWorkCall;
import com.proxy.service.api.callback.request.CloudNetWorkRequest;
import com.proxy.service.api.callback.request.NetWorkCallback;
import com.proxy.service.api.callback.response.CloudNetWorkResponse;

import java.io.IOException;

/**
 * @author : cangHX
 * on 2020/07/30  10:23 PM
 */
public class BodyNetWorkCall<T> implements CloudNetWorkCall<T> {

    private CloudNetWorkRequest mRequest;
    private NetWorkCallback mCallback;

    public BodyNetWorkCall(CloudNetWorkRequest request, NetWorkCallback callback) {
        this.mRequest = request;
        this.mCallback = callback;
    }

    /**
     * 同步请求
     *
     * @return 请求结果
     * @throws IOException 请求过程中异常
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/31 10:29 PM
     */
    @NonNull
    @Override
    public CloudNetWorkResponse<T> execute() throws IOException {
        return null;
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
    public void enqueue(@NonNull CloudNetWorkCallback<T> callback) {

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
        return false;
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
        return false;
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
