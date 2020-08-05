package com.proxy.service.network.impl;

import com.proxy.service.api.callback.request.CloudNetWorkCall;
import com.proxy.service.api.callback.request.CloudNetWorkGlobalCallback;
import com.proxy.service.api.callback.response.CloudNetWorkResponse;
import com.proxy.service.network.cache.CallCache;

/**
 * @author : cangHX
 * on 2020/08/05  5:59 PM
 */
public class GlobalRequestCallbackImpl implements CloudNetWorkGlobalCallback {
    /**
     * 开始请求
     *
     * @param call : 本次请求的 call 对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/5 5:33 PM
     */
    @Override
    public void onStart(CloudNetWorkCall<?> call) {

    }

    /**
     * 请求完成
     *
     * @param call     : 本次请求的 call 对象
     * @param response : 返回值
     * @return 是否拦截本次事件传递，true 拦截，false 不拦截
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/5 10:32 PM
     */
    @Override
    public boolean onResponse(CloudNetWorkCall<?> call, CloudNetWorkResponse<?> response) {
        return false;
    }

    /**
     * 请求失败
     *
     * @param call : 本次请求的 call 对象
     * @param t    : 失败信息
     * @return 是否拦截本次事件传递，true 拦截，false 不拦截
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/5 10:32 PM
     */
    @Override
    public boolean onFailure(CloudNetWorkCall<?> call, Throwable t) {
        return false;
    }

    /**
     * 请求结束
     *
     * @param call : 本次请求的 call 对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/5 10:32 PM
     */
    @Override
    public void onFinish(CloudNetWorkCall<?> call) {
        CallCache.notifyCancel();
    }
}
