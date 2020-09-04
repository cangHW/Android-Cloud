package com.proxy.service.api.net;

/**
 * 网络状态回调
 *
 * @author : cangHX
 * on 2020/08/28  6:09 PM
 */
public interface CloudNetWorkCallback {

    /**
     * 接收到消息
     *
     * @param type : 网络类型
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-24 18:04
     */
    void onReceive(CloudNetWorkType type);

    /**
     * 断开连接
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/31 5:12 PM
     */
    void disConnect();
}
