package com.proxy.service.network.download;

import com.proxy.service.api.download.CloudNetWorkDownloadInfo;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.api.utils.WeakReferenceUtils;
import com.proxy.service.network.download.db.DbHelper;
import com.proxy.service.network.download.db.TableDownloadInfo;
import com.proxy.service.network.download.info.DownloadInfo;
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

    private DownloadClientInterfaceImpl mClientInterface;

    private DownloadSeverInterface mTaskSeverInterface;
    private DownloadSeverInterface mTaskProcessSeverInterface;

    private DownloadManager() {
        mClientInterface = new DownloadClientInterfaceImpl(this);
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
                    info.enable = false;
                }
            });
            DownloadInfoUtils.findValueById(mInfoCacheList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                @Override
                public void onFound(CloudNetWorkDownloadInfo info) {
                    info.enable = false;
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
                    info.enable = true;
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

        private WeakReference<DownloadManager> weakReference;

        private DownloadClientInterfaceImpl(DownloadManager downloadManager) {
            weakReference = new WeakReference<>(downloadManager);
        }

        @Override
        public void onStart(final int downloadId) {
            WeakReferenceUtils.checkValueIsEmpty(weakReference, new WeakReferenceUtils.Callback<DownloadManager>() {
                @Override
                public void onCallback(DownloadManager downloadManager) {
                    DownloadInfoUtils.findValueById(downloadManager.mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                        @Override
                        public void onFound(CloudNetWorkDownloadInfo info) {
                            if (info.enable) {
                                info.getDownloadCallback().onStart(info);
                            } else {
                                info.getDownloadCallback().onContinue(info);
                            }
                            info.enable = true;
                        }
                    });
                }
            });
        }

        @Override
        public void onProgress(final int downloadId, final long progress, final long total) {
            WeakReferenceUtils.checkValueIsEmpty(weakReference, new WeakReferenceUtils.Callback<DownloadManager>() {
                @Override
                public void onCallback(DownloadManager downloadManager) {
                    DownloadInfoUtils.findValueById(downloadManager.mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                        @Override
                        public void onFound(CloudNetWorkDownloadInfo info) {
                            info.getDownloadCallback().onProgress(info, progress, total);
                        }
                    });
                }
            });
        }

        @Override
        public void onPause(final int downloadId) {
            WeakReferenceUtils.checkValueIsEmpty(weakReference, new WeakReferenceUtils.Callback<DownloadManager>() {
                @Override
                public void onCallback(DownloadManager downloadManager) {
                    DownloadInfoUtils.findValueById(downloadManager.mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                        @Override
                        public void onFound(CloudNetWorkDownloadInfo info) {
                            info.getDownloadCallback().onPause(info);
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
                        public void onFound(CloudNetWorkDownloadInfo info) {
                            downloadManager.mInfoList.remove(info);
                            info.getDownloadCallback().onSuccess(info);
                        }
                    });

                    List<CloudNetWorkDownloadInfo> list = new ArrayList<>(downloadManager.mInfoCacheList);
                    if (list.size() > 0) {
                        CloudNetWorkDownloadInfo info = list.get(0);
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
                public void onCallback(DownloadManager downloadManager) {
                    DownloadInfoUtils.findValueById(downloadManager.mInfoList, downloadId, new DownloadInfoUtils.CheckedCallback() {
                        @Override
                        public void onFound(CloudNetWorkDownloadInfo info) {
                            info.getDownloadCallback().onWarning(warningMsg);
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
                        public void onFound(CloudNetWorkDownloadInfo info) {
                            downloadManager.mInfoList.remove(info);
                            info.getDownloadCallback().onFailed(errorMsg);
                        }
                    });

                    List<CloudNetWorkDownloadInfo> list = new ArrayList<>(downloadManager.mInfoCacheList);
                    if (list.size() > 0) {
                        CloudNetWorkDownloadInfo info = list.get(0);
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
