package com.proxy.service.network.download;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.download.CloudNetWorkDownloadInfo;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.log.Logger;
import com.proxy.service.api.log.WeakReferenceUtils;
import com.proxy.service.network.download.db.DbHelper;
import com.proxy.service.network.download.db.TableDownloadInfo;
import com.proxy.service.network.download.info.DownloadInfo;
import com.proxy.service.api.download.CloudDownloadState;
import com.proxy.service.network.download.notification.NotificationImpl;
import com.proxy.service.network.download.services.TaskService;
import com.proxy.service.network.retrofit.DownloadClientInterface;
import com.proxy.service.network.retrofit.DownloadSeverInterface;
import com.proxy.service.network.utils.DownloadInfoUtils;
import com.proxy.service.network.utils.ServiceUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/09/03  9:19 PM
 */
public class DownloadManager {

    private static final Object LOCK = new Object();

    private int mMaxCount = 5;
    private boolean mMultiProcessEnable = true;

    private final List<CloudNetWorkDownloadInfo> mInfoList = new ArrayList<>();
    private final List<CloudNetWorkDownloadInfo> mInfoCacheList = new ArrayList<>();

    private final CloudUtilsTaskService mTaskService;

    private final DownloadClientInterfaceImpl mClientInterface;
    private DownloadSeverInterface mTaskProcessSeverInterface;

    private DownloadManager() {
        mTaskService = CloudSystem.getService(CloudServiceTagUtils.UTILS_TASK);
        mClientInterface = new DownloadClientInterfaceImpl(this);
        TaskService.getInstance().setCallback(mClientInterface);
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
            boolean isHas = DownloadInfoUtils.isHasValue(mInfoList, info.getDownloadId());
            if (isHas) {
                continues(info.getDownloadId());
                Logger.Debug(CloudApiError.DATA_DUPLICATION.setMsg("Has been added. with : " + info.getFileUrl()).build());
                return;
            }
            boolean isCacheHas = DownloadInfoUtils.isHasValue(mInfoCacheList, info.getDownloadId());
            if (isCacheHas) {
                continues(info.getDownloadId());
                Logger.Debug(CloudApiError.DATA_DUPLICATION.setMsg("Has been added. with : " + info.getFileUrl()).build());
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

    public void pause(final int downloadId) {
        synchronized (LOCK) {
            boolean isHas = DownloadInfoUtils.isHasValue(mInfoList, downloadId);
            boolean isCacheHas = DownloadInfoUtils.isHasValue(mInfoCacheList, downloadId);
            if (!isHas && !isCacheHas) {
                return;
            }

            cancelTask(downloadId);
            DownloadInfoUtils.findValueById(mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                @Override
                public void onFound(CloudNetWorkDownloadInfo info) {
                    info.setPause(true);
                    info.getDownloadCallback().onPause(info);

                    final DownloadInfo downloadInfo = DbHelper.getInstance().query(TableDownloadInfo.COLUMN_FILE_URL + "=?", new String[]{info.getFileUrl()});
                    if (downloadInfo != null) {
                        downloadInfo.state = CloudDownloadState.PAUSE;
                        DbHelper.getInstance().update(downloadInfo, TableDownloadInfo.COLUMN_FILE_URL + "=?", new String[]{info.getFileUrl()});
                    }

                    NotificationImpl.getInstance().showNotification(NotificationImpl.PAUSE, info);
                }
            });
            DownloadInfoUtils.findValueById(mInfoCacheList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                @Override
                public void onFound(CloudNetWorkDownloadInfo info) {
                    info.setPause(true);
                }
            });
        }
    }

    public void continues(int downloadId) {
        synchronized (LOCK) {
            DownloadInfoUtils.findValueById(mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                @Override
                public void onFound(CloudNetWorkDownloadInfo info) {
                    if (info.isPause()) {
                        startService(info);
                    }
                }
            });
            DownloadInfoUtils.findValueById(mInfoCacheList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                @Override
                public void onFound(CloudNetWorkDownloadInfo info) {
                    info.setPause(false);
                }
            });
        }
    }

    public void cancel(int downloadId) {
        synchronized (LOCK) {
            cancelTask(downloadId);
            NotificationImpl.getInstance().cancelNotification(downloadId);
            removeValue(mInfoCacheList, downloadId);
            removeValue(mInfoList, downloadId);
        }
    }

    private static class DownloadClientInterfaceImpl extends DownloadClientInterface.Stub {

        private final WeakReference<DownloadManager> weakReference;

        private DownloadClientInterfaceImpl(DownloadManager downloadManager) {
            weakReference = new WeakReference<>(downloadManager);
        }

        @Override
        public void onStart(final int downloadId) {
            WeakReferenceUtils.checkValueIsEmpty(weakReference, new WeakReferenceUtils.Callback<DownloadManager>() {
                @Override
                public void onCallback(WeakReference<DownloadManager> weakReference, final DownloadManager downloadManager) {
                    DownloadInfoUtils.findValueById(downloadManager.mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                        @Override
                        public void onFound(final CloudNetWorkDownloadInfo info) {
                            final DownloadInfo downloadInfo = DbHelper.getInstance().query(TableDownloadInfo.COLUMN_FILE_URL + "=?", new String[]{info.getFileUrl()});
                            downloadManager.mTaskService.callUiThread(new Task<Object>() {
                                @Override
                                public Object call() {
                                    info.setProgress(CloudNetWorkDownloadInfo.PROGRESS_EMPTY);
                                    if (info.isPause()) {
                                        info.getDownloadCallback().onContinue(info);
                                        if (downloadInfo != null) {
                                            downloadInfo.state = CloudDownloadState.CONTINUES;
                                        }
                                    } else {
                                        info.getDownloadCallback().onStart(info);
                                        if (downloadInfo != null) {
                                            downloadInfo.state = CloudDownloadState.START;
                                        }
                                    }
                                    info.setPause(false);
                                    if (downloadInfo != null) {
                                        DbHelper.getInstance().update(downloadInfo, TableDownloadInfo.COLUMN_FILE_URL + "=?", new String[]{info.getFileUrl()});
                                    }
                                    NotificationImpl.getInstance().showNotification(NotificationImpl.START, info);
                                    return null;
                                }
                            });
                        }
                    });
                }
            });
        }

        @Override
        public void onProgress(final int downloadId, final long progress, final long total) {
            WeakReferenceUtils.checkValueIsEmpty(weakReference, new WeakReferenceUtils.Callback<DownloadManager>() {
                @Override
                public void onCallback(WeakReference<DownloadManager> weakReference, final DownloadManager downloadManager) {
                    DownloadInfoUtils.findValueById(downloadManager.mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                        @Override
                        public void onFound(final CloudNetWorkDownloadInfo info) {
                            final DownloadInfo downloadInfo = DbHelper.getInstance().query(TableDownloadInfo.COLUMN_FILE_URL + "=?", new String[]{info.getFileUrl()});
                            if (downloadInfo != null && downloadInfo.state != CloudDownloadState.LOADING) {
                                downloadInfo.state = CloudDownloadState.LOADING;
                                DbHelper.getInstance().update(downloadInfo, TableDownloadInfo.COLUMN_FILE_URL + "=?", new String[]{info.getFileUrl()});
                            }
                            downloadManager.mTaskService.callUiThread(new Task<Object>() {
                                @Override
                                public Object call() {
                                    info.getDownloadCallback().onProgress(info, progress, total);

                                    if (total < 0) {
                                        NotificationImpl.getInstance().showNotification(50, info);
                                    } else if (progress > total) {
                                        NotificationImpl.getInstance().showNotification(NotificationImpl.NO_PROGRESS, info);
                                    } else {
                                        int p = (int) (progress * 100 / total);
                                        if (p - 3 > info.getProgress()) {
                                            info.setProgress(p);
                                            NotificationImpl.getInstance().showNotification(p, info);
                                        } else if (progress == total) {
                                            info.setProgress(100);
                                            NotificationImpl.getInstance().showNotification(100, info);
                                        }
                                    }
                                    return null;
                                }
                            });
                        }
                    });
                }
            });
        }

        @Override
        public void onSuccess(final int downloadId) {
            WeakReferenceUtils.checkValueIsEmpty(weakReference, new WeakReferenceUtils.Callback<DownloadManager>() {
                @Override
                public void onCallback(WeakReference<DownloadManager> weakReference, final DownloadManager downloadManager) {
                    DownloadInfoUtils.findValueById(downloadManager.mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                        @Override
                        public void onFound(final CloudNetWorkDownloadInfo info) {
                            final DownloadInfo downloadInfo = DbHelper.getInstance().query(TableDownloadInfo.COLUMN_FILE_URL + "=?", new String[]{info.getFileUrl()});
                            if (downloadInfo != null) {
                                downloadInfo.state = CloudDownloadState.COMPLETED;
                                downloadInfo.finishTime = System.currentTimeMillis();
                                DbHelper.getInstance().update(downloadInfo, TableDownloadInfo.COLUMN_FILE_URL + "=?", new String[]{info.getFileUrl()});
                            }
                            downloadManager.mInfoList.remove(info);
                            downloadManager.mTaskService.callUiThread(new Task<Object>() {
                                @Override
                                public Object call() {
                                    info.getDownloadCallback().onSuccess(info);
                                    NotificationImpl.getInstance().showNotification(100, info);
                                    return null;
                                }
                            });
                        }
                    });

                    List<CloudNetWorkDownloadInfo> list = new ArrayList<>(downloadManager.mInfoCacheList);
                    for (CloudNetWorkDownloadInfo info : list) {
                        if (info.isPause()) {
                            continue;
                        }
                        downloadManager.mInfoCacheList.remove(info);
                        downloadManager.start(info);
                    }
                }
            });
        }

        @Override
        public void onWarning(final int downloadId, final String warningMsg) {
            WeakReferenceUtils.checkValueIsEmpty(weakReference, new WeakReferenceUtils.Callback<DownloadManager>() {
                @Override
                public void onCallback(WeakReference<DownloadManager> weakReference, final DownloadManager downloadManager) {
                    DownloadInfoUtils.findValueById(downloadManager.mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                        @Override
                        public void onFound(final CloudNetWorkDownloadInfo info) {
                            downloadManager.mTaskService.callUiThread(new Task<Object>() {
                                @Override
                                public Object call() {
                                    info.getDownloadCallback().onWarning(warningMsg);
                                    return null;
                                }
                            });
                        }
                    });
                }
            });
        }

        @Override
        public void onFailed(final int downloadId, final String errorMsg) {
            WeakReferenceUtils.checkValueIsEmpty(weakReference, new WeakReferenceUtils.Callback<DownloadManager>() {
                @Override
                public void onCallback(WeakReference<DownloadManager> weakReference, final DownloadManager downloadManager) {
                    DownloadInfoUtils.findValueById(downloadManager.mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                        @Override
                        public void onFound(final CloudNetWorkDownloadInfo info) {
                            final DownloadInfo downloadInfo = DbHelper.getInstance().query(TableDownloadInfo.COLUMN_FILE_URL + "=?", new String[]{info.getFileUrl()});
                            if (downloadInfo != null) {
                                downloadInfo.state = CloudDownloadState.FAILED;
                                DbHelper.getInstance().update(downloadInfo, TableDownloadInfo.COLUMN_FILE_URL + "=?", new String[]{info.getFileUrl()});
                            }
                            downloadManager.mInfoList.remove(info);
                            downloadManager.mTaskService.callUiThread(new Task<Object>() {
                                @Override
                                public Object call() {
                                    info.getDownloadCallback().onFailed(errorMsg);
                                    NotificationImpl.getInstance().showNotification(NotificationImpl.FAILED, info);
                                    return null;
                                }
                            });
                        }
                    });

                    List<CloudNetWorkDownloadInfo> list = new ArrayList<>(downloadManager.mInfoCacheList);
                    for (CloudNetWorkDownloadInfo info : list) {
                        if (info.isPause()) {
                            continue;
                        }
                        downloadManager.mInfoCacheList.remove(info);
                        downloadManager.start(info);
                    }
                }
            });
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
            TaskService.getInstance().start(createInfo(info));
        }
    }

    private DownloadInfo createInfo(CloudNetWorkDownloadInfo info) {
        DownloadInfo downloadInfo = DbHelper.getInstance().query(TableDownloadInfo.COLUMN_FILE_URL + "=?", new String[]{info.getFileUrl()});
        DownloadInfo downloadInfo1 = DownloadInfoUtils.compare(info, downloadInfo);
        if (downloadInfo1 != null) {
            DbHelper.getInstance().update(downloadInfo1, TableDownloadInfo.COLUMN_FILE_URL + "=?", new String[]{downloadInfo1.fileUrl});
            return downloadInfo1;
        }
        return downloadInfo;
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
        TaskService.getInstance().cancel(downloadId);
        if (mTaskProcessSeverInterface != null) {
            try {
                mTaskProcessSeverInterface.cancel(downloadId);
            } catch (Throwable ignored) {
            }
        }
    }
}
