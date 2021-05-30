package com.proxy.service.utils.event.lifecycle;

import com.proxy.service.api.event.Event;

import java.util.Set;

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
     * 销毁
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 8:16 PM
     */
    void destroy();

    /**
     * 添加 Event 类型
     *
     * @param set : Event 类型集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 8:16 PM
     */
    void addClasses(Set<Class<?>> set);

    /**
     * 检查是否处理当前 Event 类型
     *
     * @param aClass : Event 类型
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 8:16 PM
     */
    boolean contains(Class<?> aClass);

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
