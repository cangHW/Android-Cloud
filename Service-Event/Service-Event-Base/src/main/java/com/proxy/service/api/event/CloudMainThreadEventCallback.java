package com.proxy.service.api.event;

/**
 * 主线程回调
 *
 * @author : cangHX
 * on 2021/05/14  8:38 PM
 */
public interface CloudMainThreadEventCallback<T> extends Event {

    /**
     * 接收消息
     *
     * @param t : 透传自定义消息
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 5:51 PM
     */
    void onMainEvent(T t);

}
