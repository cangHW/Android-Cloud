package com.proxy.service.api.event;

import java.util.Set;

/**
 * 工作线程回调
 *
 * @author : cangHX
 * on 2021/05/14  8:38 PM
 */
public interface CloudWorkThreadEventCallback extends Event {

    /**
     * 接收消息
     *
     * @param object : 透传自定义消息
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 7:51 PM
     */
    void onWorkEvent(Object object);

    /**
     * 设置接收消息的类型
     *
     * @return 接收消息的类型集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 7:51 PM
     */
    Set<Class<?>> getWorkEventTypes();

}
