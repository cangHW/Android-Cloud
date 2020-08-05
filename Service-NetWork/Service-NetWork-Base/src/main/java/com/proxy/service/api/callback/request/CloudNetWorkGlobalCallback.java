package com.proxy.service.api.callback.request;

import com.proxy.service.api.callback.response.CloudNetWorkResponse;

/**
 * 全局请求回调
 *
 * @author : cangHX
 * on 2020/08/05  10:24 PM
 */
public interface CloudNetWorkGlobalCallback {

    /**
     * 开始请求
     *
     * @param call : 本次请求的 call 对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/5 5:33 PM
     */
    void onStart(CloudNetWorkCall<?> call);

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
    boolean onResponse(CloudNetWorkCall<?> call, CloudNetWorkResponse<?> response);

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
    boolean onFailure(CloudNetWorkCall<?> call, Throwable t);

    /**
     * 请求结束
     *
     * @param call : 本次请求的 call 对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/5 10:32 PM
     */
    void onFinish(CloudNetWorkCall<?> call);

}
