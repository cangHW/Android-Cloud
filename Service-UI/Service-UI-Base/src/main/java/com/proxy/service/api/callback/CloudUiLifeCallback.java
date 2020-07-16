package com.proxy.service.api.callback;

/**
 * 生命周期回调
 *
 * @author: cangHX
 * on 2020/07/01  11:12
 */
public interface CloudUiLifeCallback {

    /**
     * onResume
     * 不会同时回调 onUiVisible
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/15 1:33 PM
     */
    void onUiResume();

    /**
     * onStop
     * 不会同时回调 onUiInVisible
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/15 1:34 PM
     */
    void onUiStop();

    /**
     * 显示
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/15 1:34 PM
     */
    void onUiVisible();

    /**
     * 隐藏
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/15 1:34 PM
     */
    void onUiInVisible();

}
