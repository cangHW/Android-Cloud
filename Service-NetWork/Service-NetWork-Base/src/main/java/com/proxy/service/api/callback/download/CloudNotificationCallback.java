package com.proxy.service.api.callback.download;

/**
 * @author : cangHX
 * on 2020/9/2 7:34 PM
 */
public interface CloudNotificationCallback {

    /**
     * 通知被点击
     *
     * @param downloadId : 下载任务ID
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/29 10:31 PM
     */
    void onNotificationClick(int downloadId);

}
