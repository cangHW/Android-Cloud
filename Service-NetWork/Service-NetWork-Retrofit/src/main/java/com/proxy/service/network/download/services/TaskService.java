package com.proxy.service.network.download.services;

import android.util.SparseArray;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.network.download.info.DownloadInfo;
import com.proxy.service.network.download.listener.DownloadListener;
import com.proxy.service.network.download.task.DownloadTask;
import com.proxy.service.network.retrofit.DownloadClientInterface;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : cangHX
 * on 2020/09/03  10:20 PM
 */
public class TaskService {

    private CloudUtilsTaskService mTaskService;
    private SparseArray<DownloadTask> mTaskArrays = new SparseArray<>();
    private DownloadClientInterface.Stub mClientInterface;
    private AtomicInteger mTaskId = new AtomicInteger(0);

    private TaskService() {
        mTaskService = CloudSystem.getService(CloudServiceTagUtils.UTILS_TASK);
    }

    private static class Factory {
        private static final TaskService INSTANCE = new TaskService();
    }

    public static TaskService getInstance() {
        return Factory.INSTANCE;
    }

    public void setCallback(DownloadClientInterface.Stub clientInterface) {
        this.mClientInterface = clientInterface;
    }

    public void start(final DownloadInfo info) {
        if (mTaskService == null) {
            Logger.Debug("Please check whether to use \"exclude\" to remove partial dependencies");
            return;
        }
        mTaskService.callWorkThread(new Task<Object>() {
            @Override
            public Object call() {
                DownloadTask task = DownloadTask.create();
                task.setDownloadListener(mDownloadListener);
                mTaskArrays.put(info.downloadId, task);
                task.start(info);
                mTaskArrays.remove(info.downloadId);
                return null;
            }
        });
    }

    public void cancel(final int downloadId) {
        mTaskService.callWorkThread(new Task<Object>() {
            @Override
            public Object call() {
                DownloadTask task = mTaskArrays.get(downloadId);
                task.cancel();
                return null;
            }
        });
    }

    private DownloadListener mDownloadListener = new DownloadListener() {
        @Override
        public void onStart(DownloadInfo info) {
            if (mClientInterface != null) {
                try {
                    mClientInterface.onStart(info.downloadId);
                } catch (Throwable e) {
                    Logger.Debug(e);
                }
            }
        }

        @Override
        public void onProgress(long progress, long total, DownloadInfo info) {
            if (mClientInterface != null) {
                try {
                    mClientInterface.onProgress(info.downloadId, progress, total);
                } catch (Throwable e) {
                    Logger.Debug(e);
                }
            }
        }

        @Override
        public void onSuccess(DownloadInfo info) {
            if (mClientInterface != null) {
                try {
                    mClientInterface.onSuccess(info.downloadId);
                } catch (Throwable e) {
                    Logger.Debug(e);
                }
            }
        }

        @Override
        public void onWarning(String warningMsg, DownloadInfo info) {
            if (mClientInterface != null) {
                try {
                    mClientInterface.onWarning(info.downloadId, warningMsg);
                } catch (Throwable e) {
                    Logger.Debug(e);
                }
            }
        }

        @Override
        public void onFailed(String errorMsg, DownloadInfo info) {
            if (mClientInterface != null) {
                try {
                    mClientInterface.onFailed(info.downloadId, errorMsg);
                } catch (Throwable e) {
                    Logger.Debug(e);
                }
            }
        }
    };
}
