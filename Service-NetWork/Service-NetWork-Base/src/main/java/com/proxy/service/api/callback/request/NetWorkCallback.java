package com.proxy.service.api.callback.request;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.request.method.CloudNetWorkRequest;

/**
 * @author : cangHX
 * on 2020/07/31  10:26 PM
 */
public interface NetWorkCallback {

    /**
     * 设置请求体
     *
     * @param requestBody : 请求体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 5:11 PM
     */
    void setRequestBody(@NonNull CloudNetWorkRequest requestBody);

    /**
     * 同步请求
     *
     * @return 获取到的返回值 jsonString
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 10:55 PM
     */
    @Nullable
    String execute();

    /**
     * 异步请求
     *
     * @param callback : 请求回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 10:55 PM
     */
    void enqueue(@NonNull RequestCallback callback);

    /**
     * 取消请求
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 10:59 PM
     */
    void cancel();
}
