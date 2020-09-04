package com.proxy.service.network.download;

import android.os.RemoteException;

import com.proxy.service.api.download.CloudNetWorkDownloadInfo;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.network.download.info.DownloadInfo;
import com.proxy.service.network.retrofit.DownloadClientInterface;
import com.proxy.service.network.retrofit.DownloadSeverInterface;
import com.proxy.service.network.utils.DownloadInfoUtils;
import com.proxy.service.network.utils.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/09/03  9:19 PM
 */
public class DownloadManager {

    public static final String KEY = "CLOUD_INFO";
    private static final Object LOCK = new Object();

    private int mMaxCount = 5;
    private boolean mMultiProcessEnable = true;

    private List<CloudNetWorkDownloadInfo> mInfoList = new ArrayList<>();
    private List<CloudNetWorkDownloadInfo> mInfoCacheList = new ArrayList<>();

    private DownloadClientInterfaceImpl mClientInterface = new DownloadClientInterfaceImpl();

    private DownloadSeverInterface mTaskSeverInterface;
    private DownloadSeverInterface mTaskProcessSeverInterface;

    private DownloadManager() {
    }

    private static class Factory {
        private static final DownloadManager INSTANCE = new DownloadManager();
    }

    public static DownloadManager getInstance() {
        return Factory.INSTANCE;
    }

    public void setMaxCount(int maxCount) {
        this.mMaxCount = maxCount;
    }

    public void setMultiProcessEnable(boolean multiProcessEnable) {
        this.mMultiProcessEnable = multiProcessEnable;
    }

    public void start(CloudNetWorkDownloadInfo info) {
        synchronized (LOCK) {
            boolean isHas = DownloadInfoUtils.isHasValue(mInfoList, info);
            if (isHas) {
                return;
            }
            if (mInfoList.size() == mMaxCount) {
                mInfoCacheList.add(info);
                return;
            }
            mInfoList.add(info);
            startService(info);
        }
    }

    public void pause(int downloadId) {
        synchronized (LOCK) {
            cancelTask(downloadId);
            DownloadInfoUtils.findValueById(mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                @Override
                public void onFound(CloudNetWorkDownloadInfo info) {
                    info.setEnable(false);
                }
            });
            DownloadInfoUtils.findValueById(mInfoCacheList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                @Override
                public void onFound(CloudNetWorkDownloadInfo info) {
                    info.setEnable(false);
                }
            });
        }
    }

    public void continues(int downloadId) {
        synchronized (LOCK) {
            DownloadInfoUtils.findValueById(mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                @Override
                public void onFound(CloudNetWorkDownloadInfo info) {
                    info.setEnable(true);
                    startService(info);
                }
            });
            DownloadInfoUtils.findValueById(mInfoCacheList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                @Override
                public void onFound(CloudNetWorkDownloadInfo info) {
                    info.setEnable(true);
                }
            });
        }
    }

    public void cancel(int downloadId) {
        synchronized (LOCK) {
            removeValue(mInfoCacheList, downloadId);
            removeValue(mInfoList, downloadId);
            cancelTask(downloadId);
        }
    }

    private static class DownloadClientInterfaceImpl extends DownloadClientInterface.Stub {

        @Override
        public void onStart(int downloadId) throws RemoteException {

        }

        @Override
        public void onProgress(int downloadId, long progress, long total) throws RemoteException {

        }

        @Override
        public void onPause(int downloadId) throws RemoteException {

        }

        @Override
        public void onSuccess(int downloadId) throws RemoteException {

        }

        @Override
        public void onFailed(int downloadId, String errType) throws RemoteException {

        }
    }

    private void startService(CloudNetWorkDownloadInfo info) {
        if (mMultiProcessEnable) {
            ServiceUtils.startTaskProcessService(createInfo(info), new ServiceUtils.StartCallback() {
                @Override
                public void onSuccess(DownloadSeverInterface severInterface) {
                    try {
                        mTaskProcessSeverInterface = severInterface;
                        severInterface.addCallback(mClientInterface);
                    } catch (Throwable e) {
                        Logger.Debug(e);
                    }
                }

                @Override
                public void onFailed() {
                }

                @Override
                public void onDisconnected() {
                    mTaskProcessSeverInterface = null;
                }
            });
        } else {
            ServiceUtils.startTaskService(createInfo(info), new ServiceUtils.StartCallback() {
                @Override
                public void onSuccess(DownloadSeverInterface severInterface) {
                    try {
                        mTaskSeverInterface = severInterface;
                        severInterface.addCallback(mClientInterface);
                    } catch (Throwable e) {
                        Logger.Debug(e);
                    }
                }

                @Override
                public void onFailed() {
                }

                @Override
                public void onDisconnected() {
                    mTaskSeverInterface = null;
                }
            });
        }
    }

    private DownloadInfo createInfo(CloudNetWorkDownloadInfo info) {
        //todo
        return null;
    }

    private void removeValue(List<CloudNetWorkDownloadInfo> list, int id) {
        for (CloudNetWorkDownloadInfo info : new ArrayList<>(list)) {
            if (info.getDownloadId() != id) {
                continue;
            }
            list.remove(info);
            break;
        }
    }

    private void cancelTask(int downloadId) {
        if (mTaskSeverInterface != null) {
            try {
                mTaskSeverInterface.cancel(downloadId);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
        if (mTaskProcessSeverInterface != null) {
            try {
                mTaskProcessSeverInterface.cancel(downloadId);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }
}
