package com.proxy.service.utils.event.lifecycle;

import com.proxy.service.api.event.Event;

/**
 * @author : cangHX
 * on 2021/05/20  8:16 PM
 */
public interface Lifecycle {

    /**
     * 是否已被销毁
     *
     * @return true 已销毁，false 未销毁
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 8:16 PM
     */
    boolean isDestroy();

    /**
     * 是否相同
     *
     * @param event : 接口对象
     * @return true 相同，false 不同
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 8:27 PM
     */
    boolean isSame(Event event);

    /**
     * 发送同步消息
     *
     * @param object : 消息体
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 8:16 PM
     */
    void set(final Object object);

    /**
     * 发送异步消息
     *
     * @param object : 消息体
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 8:16 PM
     */
    void post(Object object);
}
