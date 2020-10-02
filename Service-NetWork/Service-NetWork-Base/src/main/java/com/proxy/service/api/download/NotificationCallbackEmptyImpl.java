package com.proxy.service.api.download;

import com.proxy.service.api.callback.download.CloudNotificationCallback;

/**
 * @author : cangHX
 * on 2020/09/28  10:32 PM
 */
public class NotificationCallbackEmptyImpl implements CloudNotificationCallback {
    /**
     * 通知被点击
     *
     * @param downloadId : 下载任务ID
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/29 10:31 PM
     */
    @Override
    public void onNotificationClick(int downloadId) {

    }
}
