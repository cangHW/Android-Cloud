package com.proxy.service.network.download;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.download.CloudNetWorkDownloadInfo;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.api.utils.WeakReferenceUtils;
import com.proxy.service.network.download.db.DbHelper;
import com.proxy.service.network.download.db.TableDownloadInfo;
import com.proxy.service.network.download.info.DownloadInfo;
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

    private List<CloudNetWorkDownloadInfo> mInfoList = new ArrayList<>();
    private List<CloudNetWorkDownloadInfo> mInfoCacheList = new ArrayList<>();

    private CloudUtilsTaskService mTaskService;

    private DownloadClientInterfaceImpl mClientInterface;
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
            boolean isHas = DownloadInfoUtils.isHasValue(mInfoList, info.downloadId);
            if (isHas) {
                Logger.Debug(CloudApiError.DATA_DUPLICATION.setMsg("Has been added. with : " + info.getFileUrl()).build());
                return;
            }
            boolean isCacheHas = DownloadInfoUtils.isHasValue(mInfoCacheList, info.downloadId);
            if (isCacheHas) {
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

    public void pause(int downloadId) {
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
                    info.isPause = true;
                    NotificationImpl.getInstance().showNotification(NotificationImpl.PAUSE, info);
                }
            });
            DownloadInfoUtils.findValueById(mInfoCacheList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                @Override
                public void onFound(CloudNetWorkDownloadInfo info) {
                    info.isPause = true;
                }
            });
        }
    }

    public void continues(int downloadId) {
        synchronized (LOCK) {
            DownloadInfoUtils.findValueById(mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                @Override
                public void onFound(CloudNetWorkDownloadInfo info) {
                    startService(info);
                }
            });
            DownloadInfoUtils.findValueById(mInfoCacheList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                @Override
                public void onFound(CloudNetWorkDownloadInfo info) {
                    info.isPause = false;
                }
            });
        }
    }

    public void cancel(int downloadId) {
        synchronized (LOCK) {
            cancelTask(downloadId);
            DownloadInfoUtils.findValueById(mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                @Override
                public void onFound(CloudNetWorkDownloadInfo info) {
                    NotificationImpl.getInstance().cancelNotification(info);
                }
            });
            removeValue(mInfoCacheList, downloadId);
            removeValue(mInfoList, downloadId);
        }
    }

    private static class DownloadClientInterfaceImpl extends DownloadClientInterface.Stub {

        private WeakReference<DownloadManager> weakReference;

        private DownloadClientInterfaceImpl(DownloadManager downloadManager) {
            weakReference = new WeakReference<>(downloadManager);
        }

        @Override
        public void onStart(final int downloadId) {
            WeakReferenceUtils.checkValueIsEmpty(weakReference, new WeakReferenceUtils.Callback<DownloadManager>() {
                @Override
                public void onCallback(final DownloadManager downloadManager) {
                    DownloadInfoUtils.findValueById(downloadManager.mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                        @Override
                        public void onFound(final CloudNetWorkDownloadInfo info) {
                            downloadManager.mTaskService.callUiThread(new Task<Object>() {
                                @Override
                                public Object call() {
                                    if (info.isPause) {
                                        info.getDownloadCallback().onContinue(info);
                                    } else {
                                        info.getDownloadCallback().onStart(info);
                                    }
                                    info.isPause = false;

                                    NotificationImpl.getInstance().showNotification(0, info);
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
                public void onCallback(final DownloadManager downloadManager) {
                    DownloadInfoUtils.findValueById(downloadManager.mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                        @Override
                        public void onFound(final CloudNetWorkDownloadInfo info) {
                            downloadManager.mTaskService.callUiThread(new Task<Object>() {
                                @Override
                                public Object call() {
                                    info.getDownloadCallback().onProgress(info, progress, total);

                                    if (total < 0) {
                                        NotificationImpl.getInstance().showNotification(50, info);
                                    } else if (progress > total) {
                                        NotificationImpl.getInstance().showNotification(NotificationImpl.NO_PROGRESS, info);
                                    } else {
                                        NotificationImpl.getInstance().showNotification((int) (progress / total), info);
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
                public void onCallback(final DownloadManager downloadManager) {
                    DownloadInfoUtils.findValueById(downloadManager.mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                        @Override
                        public void onFound(final CloudNetWorkDownloadInfo info) {
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
                        if (info.isPause) {
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
                public void onCallback(final DownloadManager downloadManager) {
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
                public void onCallback(final DownloadManager downloadManager) {
                    DownloadInfoUtils.findValueById(downloadManager.mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                        @Override
                        public void onFound(final CloudNetWorkDownloadInfo info) {
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
                        if (info.isPause) {
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
            if (info.downloadId != id) {
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
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }
}
