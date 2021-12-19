package com.proxy.service.media.callback;

/**
 * @author: cangHX
 * @date: 2021/12/12 21:21
 * @version: 1.0
 * @desc:
 */
public interface SurfaceStateCallback {

    /**
     * 视图创建
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/14 00:40
     */
    void onCreate();

    /**
     * 视图销毁
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/14 00:40
     */
    void onDestroy();

}
