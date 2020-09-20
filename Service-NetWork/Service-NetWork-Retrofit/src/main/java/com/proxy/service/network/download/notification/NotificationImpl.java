package com.proxy.service.network.download.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.SparseArray;

import androidx.core.app.NotificationCompat;

import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.download.CloudNetWorkDownloadInfo;
import com.proxy.service.api.download.CloudNetWorkNotificationInfo;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;

/**
 * @author : cangHX
 * on 2020/09/03  10:08 PM
 */
public class NotificationImpl {

    public static final int FAILED = -1;
    public static final int NO_PROGRESS = -2;
    public static final int PAUSE = 999;

    private NotificationManager mManager;

    private CloudNetWorkNotificationInfo mNotificationBuilder;
    private SparseArray<NotificationCompat.Builder> mBuilderArray = new SparseArray<>();

    private NotificationImpl() {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private static class Factory {
        private static final NotificationImpl INSTANCE = new NotificationImpl();
    }

    public static NotificationImpl getInstance() {
        return Factory.INSTANCE;
    }

    public void setNotificationBuilder(CloudNetWorkNotificationInfo notificationBuilder) {
        if (notificationBuilder.isError()) {
            return;
        }
        this.mNotificationBuilder = notificationBuilder;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        Context context = ContextManager.getApplication();
        if (context == null) {
            this.mNotificationBuilder = this.mNotificationBuilder.build().setChannelId("").build();
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }

        try {
            NotificationChannel channel = new NotificationChannel(notificationBuilder.getChannelId(), notificationBuilder.getChannelName(), notificationBuilder.getChannelLevel().getLevel());
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
    }

    public void showNotification(int progress, CloudNetWorkDownloadInfo info) {
        if (!info.getNotificationEnable()) {
            return;
        }
        if (mNotificationBuilder == null || mNotificationBuilder.isError()) {
            Logger.Debug("Please check the NotificationBuilder");
            return;
        }
        NotificationCompat.Builder builder = mBuilderArray.get(info.downloadId);
        if (builder == null) {
            builder = createNotificationBuilder();
            mBuilderArray.put(info.downloadId, builder);
        }
        if (builder == null) {
            Logger.Debug("Create Notification failed");
            return;
        }
        switch (progress) {
            case PAUSE:
                builder.setContentText(info.getTaskName() + ": 下载(暂停中)");
                break;
            case NO_PROGRESS:
                builder.setContentText(info.getTaskName() + ": 正在下载...");
                builder.setProgress(0, 0, true);
                break;
            case FAILED:
                builder.setContentText(info.getTaskName() + ": 下载失败");
                builder.setProgress(100, 100, true);
                break;
            default:
                if (progress == 0) {
                    builder.setContentText(info.getTaskName() + ": 开始下载");
                } else if (progress == 100) {
                    builder.setContentText(info.getTaskName() + ": 下载完成");
                } else {
                    builder.setContentText(info.getTaskName() + ": 正在下载...");
                }
                builder.setProgress(100, progress, false);
                break;
        }
        mManager.notify(info.downloadId, builder.build());
    }

    public void cancelNotification(CloudNetWorkDownloadInfo info) {
        if (!info.getNotificationEnable()) {
            return;
        }
        if (mNotificationBuilder == null || mNotificationBuilder.isError()) {
            Logger.Debug("Please check the NotificationBuilder");
            return;
        }
        if (mManager != null) {
            mManager.cancel(info.downloadId);
        }
    }

    private NotificationCompat.Builder createNotificationBuilder() {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return null;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, mNotificationBuilder.getChannelId());
        if (mNotificationBuilder.getLargeIcon() != null) {
            builder.setLargeIcon(mNotificationBuilder.getLargeIcon());
        }
        if (mNotificationBuilder.getSmallIconResId() != 0) {
            builder.setSmallIcon(mNotificationBuilder.getSmallIconResId());
        }
        return builder;
    }
}
