package com.proxy.service.api.callback;

/**
 * 变量信息检查回调
 *
 * @author: cangHX
 * on 2020/07/07  17:01
 */
public interface CloudUiFieldCheckErrorCallback {

    /**
     * 发现错误回调
     *
     * @param msg : 错误信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-08 15:41
     */
    void onError(String msg);

}
