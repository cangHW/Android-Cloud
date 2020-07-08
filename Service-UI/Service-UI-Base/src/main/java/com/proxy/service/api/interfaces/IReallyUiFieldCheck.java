package com.proxy.service.api.interfaces;

/**
 * @author: cangHX
 * on 2020/07/08  09:31
 */
public interface IReallyUiFieldCheck extends IUiFieldCheck {

    /**
     * 检测成功执行，主线程
     *
     * @param runnable : 运行体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 18:45
     */
    void runUi(Runnable runnable);

    /**
     * 检测成功执行，子线程
     *
     * @param runnable : 运行体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 18:45
     */
    void runBg(Runnable runnable);
}
