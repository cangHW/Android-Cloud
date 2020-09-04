package com.proxy.service.network.download.notification;

import com.proxy.service.api.download.CloudNetWorkNotificationInfo;

/**
 * @author : cangHX
 * on 2020/09/03  10:08 PM
 */
public class INotificationImpl implements INotification {

    private CloudNetWorkNotificationInfo mNotificationBuilder;

    private INotificationImpl() {
    }

    private static class Factory {
        private static final INotificationImpl INSTANCE = new INotificationImpl();
    }

    public static INotificationImpl getInstance() {
        return Factory.INSTANCE;
    }

    public void setNotificationBuilder(CloudNetWorkNotificationInfo notificationBuilder) {
        this.mNotificationBuilder = notificationBuilder;
    }
}
