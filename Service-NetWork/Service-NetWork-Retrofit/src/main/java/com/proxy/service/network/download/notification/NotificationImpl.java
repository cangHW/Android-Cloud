package com.proxy.service.network.download.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.SparseArray;

import androidx.core.app.NotificationCompat;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.callback.download.CloudNotificationCallback;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.download.CloudNetWorkDownloadInfo;
import com.proxy.service.api.download.CloudNetWorkNotificationInfo;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.receiver.CloudReceiverInfo;
import com.proxy.service.api.receiver.CloudReceiverListener;
import com.proxy.service.api.services.CloudUtilsAppService;
import com.proxy.service.api.services.CloudUtilsReceiverService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;

import java.lang.ref.WeakReference;

/**
 * @author : cangHX
 * on 2020/09/03  10:08 PM
 */
public class NotificationImpl implements CloudReceiverListener {

    private static final String ACTION_NOTIFICATION_CLICK = "action_notification_click";
    private static final String DOWNLOAD_ID = "notification_id";

    public static final int FAILED = -1;
    public static final int NO_PROGRESS = -2;
    public static final int START = -999;
    public static final int PAUSE = 999;

    private NotificationManager mManager;

    private String mPackageName = "";
    private CloudNetWorkNotificationInfo mNotificationBuilder;
    private final SparseArray<NotificationCompat.Builder> mBuilderArray = new SparseArray<>();
    private final SparseArray<WeakReference<CloudNotificationCallback>> mCallbackArray = new SparseArray<>();

    private NotificationImpl() {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return;
        }
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        CloudUtilsReceiverService receiverService = CloudSystem.getService(CloudUtilsReceiverService.class);
        if (receiverService != null) {
            receiverService.addReceiverListener(
                    CloudReceiverInfo
                            .builder()
                            .setAction(ACTION_NOTIFICATION_CLICK)
                            .build(),
                    this);
        }
        CloudUtilsAppService appService = CloudSystem.getService(CloudServiceTagUtils.UTILS_APP);
        if (appService != null) {
            mPackageName = appService.getPackageName();
        }
    }

    /**
     * 接收到消息
     *
     * @param context : 上下文环境
     * @param intent  : 意图
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/09/29  11:38 PM
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!ACTION_NOTIFICATION_CLICK.equals(action)) {
            return;
        }
        String pkg = intent.getPackage();
        if (!mPackageName.equals(pkg)) {
            return;
        }
        int downloadId = intent.getIntExtra(DOWNLOAD_ID, -1);
        WeakReference<CloudNotificationCallback> weakReference = mCallbackArray.get(downloadId);
        if (weakReference == null) {
            return;
        }
        CloudNotificationCallback callback = weakReference.get();
        if (callback == null) {
            mCallbackArray.remove(downloadId);
            return;
        }
        try {
            callback.onNotificationClick(downloadId);
        } catch (Throwable ignored) {
        }
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
        NotificationCompat.Builder builder = mBuilderArray.get(info.getDownloadId());
        if (builder == null) {
            builder = createNotificationBuilder(info);
            mBuilderArray.put(info.getDownloadId(), builder);
        }
        if (builder == null) {
            Logger.Debug("Create Notification failed");
            return;
        }
        switch (progress) {
            case START:
                builder.setContentTitle(info.getTaskName() + ": 开始下载");
                if (info.getProgress() == 0) {
                    builder.setProgress(100, 0, false);
                }
                if (info.getNotificationCallback() != null) {
                    mCallbackArray.put(info.getDownloadId(), new WeakReference<>(info.getNotificationCallback()));
                }
                builder.setWhen(System.currentTimeMillis());
                break;
            case PAUSE:
                builder.setContentTitle(info.getTaskName() + ": 下载(暂停中)");
                break;
            case NO_PROGRESS:
                builder.setContentTitle(info.getTaskName() + ": 正在下载...");
                builder.setProgress(0, 0, true);
                break;
            case FAILED:
                builder.setContentTitle(info.getTaskName() + ": 下载失败");
                builder.setProgress(100, 0, true);
                break;
            default:
                if (progress == 0) {
                    builder.setContentTitle(info.getTaskName() + ": 开始下载");
                } else if (progress == 100) {
                    builder.setContentTitle(info.getTaskName() + ": 下载完成");

                } else {
                    builder.setContentTitle(info.getTaskName() + ": 正在下载...");
                }
                builder.setProgress(100, progress, false);
                break;
        }
        mManager.notify(info.getDownloadId(), builder.build());
    }

    public void cancelNotification(int id) {
        if (mNotificationBuilder == null || mNotificationBuilder.isError()) {
            return;
        }
        if (mManager != null) {
            mManager.cancel(id);
        }
    }

    private NotificationCompat.Builder createNotificationBuilder(CloudNetWorkDownloadInfo info) {
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
        builder.setTicker("开始下载");
        Intent intent = new Intent(ACTION_NOTIFICATION_CLICK);
        intent.putExtra(DOWNLOAD_ID, info.getDownloadId());
        intent.setPackage(mPackageName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        return builder;
    }
}
