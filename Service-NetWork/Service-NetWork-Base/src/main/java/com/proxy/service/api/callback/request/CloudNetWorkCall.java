package com.proxy.service.api.callback.request;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.response.CloudNetWorkResponse;
import com.proxy.service.api.method.CloudNetWorkRequest;

/**
 * 默认请求回调
 *
 * @author : cangHX
 * on 2020/07/27  7:24 PM
 */
public interface CloudNetWorkCall<T> {

    /**
     * 同步请求
     *
     * @return 请求结果
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/31 10:29 PM
     */
    @NonNull
    CloudNetWorkResponse<T> execute();

    /**
     * 异步请求
     *
     * @param callback : 请求回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/31 10:30 PM
     */
    void enqueue(@NonNull CloudNetWorkCallback<T> callback);

    /**
     * 是否开始执行
     *
     * @return true 已经开始执行，false 未执行
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/31 10:31 PM
     */
    boolean isExecuted();

    /**
     * 取消请求
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/31 10:32 PM
     */
    void cancel();

    /**
     * 请求是否已经取消
     *
     * @return true 已取消，false 未取消
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/31 10:32 PM
     */
    boolean isCanceled();

    /**
     * 获取请求体
     *
     * @return 请求体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/31 10:36 PM
     */
    @NonNull
    CloudNetWorkRequest request();
}
