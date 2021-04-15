package com.proxy.service.api.action;

/**
 * @author : cangHX
 * on 2021/04/11  6:08 PM
 */
public interface Action<T> {

    /**
     * 事件回调
     *
     * @param t : 事件相关数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 6:09 PM
     */
    void onAction(T t);

}
