package com.proxy.service.network.download.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.SparseArray;

import androidx.annotation.Nullable;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.network.download.info.DownloadInfo;
import com.proxy.service.network.download.listener.DownloadListener;
import com.proxy.service.network.download.task.DownloadTask;
import com.proxy.service.network.retrofit.DownloadClientInterface;
import com.proxy.service.network.retrofit.DownloadSeverInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : cangHX
 * on 2020/09/10  9:27 PM
 */
public class DownloadService extends Service {

    private static final String TAG = "DownloadService";

    private static final String CHANNEL_ID = "cloud_service_download";
    private static final String CHANNEL_NAME = "下载";

    public static final String KEY = "INFO";

    private final SparseArray<DownloadTask> mTaskArrays = new SparseArray<>();

    private ThreadDiedCallbackList mCallbackList;
    private final DownloadBinder mBinder = new DownloadBinder();
    private final DownloadListener mDownloadListener = new DownloadListenerImpl();
    private final List<Runnable> mRunnableList = new ArrayList<>();

    private final AtomicBoolean isBind = new AtomicBoolean(false);
    private final AtomicInteger mTaskCount = new AtomicInteger(0);

    private CloudUtilsTaskService mTaskService;

    @Override
    public void onCreate() {
        super.onCreate();
        mCallbackList = new ThreadDiedCallbackList();
        mTaskService = CloudSystem.getService(CloudServiceTagUtils.UTILS_TASK);

        showNotification();
    }

    private void showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (manager != null) {
                    manager.createNotificationChannel(channel);
                    Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID).build();
                    startForeground(1, notification);
                    stopForeground(true);
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        final DownloadInfo info = intent.getParcelableExtra(KEY);
        if (info != null) {
            if (isBind.get()) {
                startTask(info, startId);
            } else {
                mRunnableList.add(new Runnable() {
                    @Override
                    public void run() {
                        startTask(info, startId);
                    }
                });
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startTask(final DownloadInfo info, int startId) {
        if (mTaskService == null) {
            Logger.Debug("Please check whether to use \"exclude\" to remove partial dependencies");
            return;
        }
        mTaskCount.incrementAndGet();
        info.serviceId = startId;
        mTaskService.callWorkThread(new Task<Object>() {
            @Override
            public Object call() {
                DownloadTask task = DownloadTask.create();
                task.setDownloadListener(mDownloadListener);
                mTaskArrays.put(info.downloadId, task);
                task.start(info);
                mTaskArrays.remove(info.downloadId);
                checkTask();
                stopSelf(info.serviceId);
                return null;
            }
        });
    }

    private void checkTask() {
        if (mTaskCount.get() == 0) {
            stopForeground(false);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private class DownloadListenerImpl implements DownloadListener {
        @Override
        public void onStart(DownloadInfo info) {
            int count = mCallbackList.beginBroadcast();
            for (int i = 0; i < count; i++) {
                DownloadClientInterface clientInterface = mCallbackList.getBroadcastItem(i);
                try {
                    clientInterface.onStart(info.downloadId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mCallbackList.finishBroadcast();
        }

        @Override
        public void onProgress(long progress, long total, DownloadInfo info) {
            int count = mCallbackList.beginBroadcast();
            for (int i = 0; i < count; i++) {
                DownloadClientInterface clientInterface = mCallbackList.getBroadcastItem(i);
                try {
                    clientInterface.onProgress(info.downloadId, progress, total);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mCallbackList.finishBroadcast();
        }

        @Override
        public void onSuccess(DownloadInfo info) {
            int count = mCallbackList.beginBroadcast();
            for (int i = 0; i < count; i++) {
                DownloadClientInterface clientInterface = mCallbackList.getBroadcastItem(i);
                try {
                    clientInterface.onSuccess(info.downloadId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mCallbackList.finishBroadcast();
        }

        @Override
        public void onWarning(String warningMsg, DownloadInfo info) {
            mTaskCount.decrementAndGet();
            int count = mCallbackList.beginBroadcast();
            for (int i = 0; i < count; i++) {
                DownloadClientInterface clientInterface = mCallbackList.getBroadcastItem(i);
                try {
                    clientInterface.onWarning(info.downloadId, warningMsg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mCallbackList.finishBroadcast();
        }

        @Override
        public void onFailed(String errorMsg, DownloadInfo info) {
            mTaskCount.decrementAndGet();
            int count = mCallbackList.beginBroadcast();
            for (int i = 0; i < count; i++) {
                DownloadClientInterface clientInterface = mCallbackList.getBroadcastItem(i);
                try {
                    clientInterface.onFailed(info.downloadId, errorMsg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mCallbackList.finishBroadcast();
        }
    }

    private class DownloadBinder extends DownloadSeverInterface.Stub {
        @Override
        public void addCallback(DownloadClientInterface callback) {
            mCallbackList.register(callback);
            isBind.set(true);
            for (Runnable runnable : new ArrayList<>(mRunnableList)) {
                runnable.run();
            }
            mRunnableList.clear();
        }

        @Override
        public void removeCallback(DownloadClientInterface callback) {
            mCallbackList.unregister(callback);
            isBind.set(false);
        }

        @Override
        public void cancel(int downloadId) {
            DownloadTask task = mTaskArrays.get(downloadId);
            task.cancel();
        }
    }

    private class ThreadDiedCallbackList extends RemoteCallbackList<DownloadClientInterface> {

        @Override
        public void onCallbackDied(DownloadClientInterface callback) {
            //应用退出，移除所有下载任务（如果发起下载任务的进程数量大于1，则需要具体判断是否移除所有任务）
            try {
                mCallbackList.unregister(callback);
                int count = mCallbackList.beginBroadcast();
                if (count == 0) {
                    stopForeground(true);
                    if (mTaskService != null) {
                        mTaskService.cancel();
                    }
                }
                mCallbackList.finishBroadcast();
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCallbackList.kill();
    }

}
