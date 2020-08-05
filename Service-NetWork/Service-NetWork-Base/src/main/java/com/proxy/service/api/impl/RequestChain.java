package com.proxy.service.api.impl;

import androidx.annotation.NonNull;

import com.proxy.service.api.base.CloudNetWorkInterceptor;
import com.proxy.service.api.callback.request.CloudNetWorkCall;
import com.proxy.service.api.callback.response.CloudNetWorkResponse;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.method.CloudNetWorkRequest;
import com.proxy.service.api.utils.Logger;

import java.util.ArrayList;

/**
 * @author : cangHX
 * on 2020/08/04  10:28 AM
 */
public class RequestChain implements CloudNetWorkInterceptor.Chain {

    private CloudNetWorkRequest request;
    private CloudNetWorkCall<?> call;
    private ArrayList<CloudNetWorkInterceptor> interceptors = new ArrayList<>();
    private int index;

    public RequestChain(CloudNetWorkRequest request, CloudNetWorkCall<?> call, ArrayList<CloudNetWorkInterceptor> interceptors, int index) {
        this.request = request;
        this.call = call;
        this.interceptors.clear();
        this.interceptors.addAll(interceptors);
        this.index = index;
    }

    /**
     * 获取请求体
     *
     * @return 请求体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/4 10:18 PM
     */
    @NonNull
    @Override
    public CloudNetWorkRequest request() {
        return request;
    }

    /**
     * 向下执行
     *
     * @param request : 请求体
     * @return 返回体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/4 10:19 PM
     */
    @NonNull
    @Override
    public <T> CloudNetWorkResponse<T> proceed(@NonNull CloudNetWorkRequest request) {
        if (index < 0 || index >= interceptors.size()) {
            Logger.Error(CloudApiError.INDEX_OUT_OF_BOUNDS.append("total is " + interceptors.size() + " and index : " + index).build());
            return CloudNetWorkResponse.error(new IndexOutOfBoundsException("total : " + interceptors.size() + " index : " + index));
        }
        CloudNetWorkInterceptor interceptor = interceptors.get(index);
        RequestChain chain = new RequestChain(request, call, interceptors, ++index);
        return interceptor.intercept(chain);
    }

    /**
     * 获取 call 对象
     *
     * @return call 对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/4 10:20 PM
     */
    @NonNull
    @Override
    public CloudNetWorkCall<?> call() {
        return call;
    }
}
