package com.proxy.service.api.base;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.request.CloudNetWorkCall;
import com.proxy.service.api.callback.response.CloudNetWorkResponse;
import com.proxy.service.api.method.CloudNetWorkRequest;

/**
 * 网络拦截器
 *
 * @author : cangHX
 * on 2020/07/19  7:14 PM
 */
public interface CloudNetWorkInterceptor {

    /**
     * 拦截器执行体
     *
     * @param chain : 链条
     * @return 返回体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/4 10:18 PM
     */
    <T> CloudNetWorkResponse<T> intercept(@NonNull Chain chain);


    interface Chain {
        /**
         * 获取请求体
         *
         * @return 请求体
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/8/4 10:18 PM
         */
        @NonNull
        CloudNetWorkRequest request();

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
        <T> CloudNetWorkResponse<T> proceed(@NonNull CloudNetWorkRequest request);

        /**
         * 获取 call 对象
         *
         * @return call 对象
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/8/4 10:20 PM
         */
        @NonNull
        CloudNetWorkCall<?> call();
    }

}
