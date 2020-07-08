package com.proxy.service.api.callback;

/**
 * @author: cangHX
 * on 2020/06/29  14:14
 * <p>
 * 事件回调
 */
public interface CloudUiEventCallback {

    /**
     * 接收到回调
     *
     * @param tabIndex : 标示发送本次事件的tab
     * @param tag      : 保留对象，开发者可以自定义设置
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:15
     */
    void onCall(int tabIndex, Object tag);

}
