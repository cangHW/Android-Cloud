package com.proxy.service.api.monitor.visible;

/**
 * @author : cangHX
 * on 2020/11/11  9:12 PM
 */
public interface VisibleMonitorHelper {

    /**
     * 开始
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    void start();

    /**
     * 还原数据，重置为原始状态
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    void reset();

    /**
     * 暂停
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    void stop();

    /**
     * 销毁
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/11 9:18 PM
     */
    void destroy();
}
