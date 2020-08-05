package com.proxy.service.api.callback.request;

import androidx.annotation.Nullable;

/**
 * @author : cangHX
 * on 2020/08/03  10:58 PM
 */
public interface RequestCallback {

    /**
     * 请求完成
     *
     * @param code       : 请求返回码
     * @param jsonString : 返回值
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 4:59 PM
     */
    void onResponse(int code, @Nullable String jsonString);

    /**
     * 请求失败
     *
     * @param t : 失败信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 4:59 PM
     */
    void onFailure(@Nullable Throwable t);

}
