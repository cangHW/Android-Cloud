package com.proxy.service.api.callback.request;

import com.proxy.service.api.callback.response.CloudNetWorkResponse;

/**
 * @author : cangHX
 * on 2020/07/31  11:53 AM
 */
public interface CloudNetWorkCallback<T> {

    /**
     * 请求完成
     *
     * @param response : 返回值
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 4:59 PM
     */
    void onResponse(CloudNetWorkResponse<T> response);

    /**
     * 请求失败
     *
     * @param t : 失败信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 4:59 PM
     */
    void onFailure(Throwable t);

}
