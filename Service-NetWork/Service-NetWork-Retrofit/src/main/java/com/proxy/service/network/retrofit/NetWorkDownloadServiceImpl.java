package com.proxy.service.network.retrofit;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.callback.download.CloudDownloadCallback;
import com.proxy.service.api.callback.download.CloudNotificationCallback;
import com.proxy.service.api.download.CloudNetWorkDownloadInfo;
import com.proxy.service.api.download.CloudNetWorkNotificationInfo;
import com.proxy.service.api.services.CloudNetWorkDownloadService;
import com.proxy.service.api.services.CloudUtilsFileService;
import com.proxy.service.api.tag.CloudServiceTagNetWork;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.network.download.DownloadManager;
import com.proxy.service.network.download.db.DbHelper;
import com.proxy.service.network.download.db.TableDownloadInfo;
import com.proxy.service.network.download.info.DownloadInfo;
import com.proxy.service.api.download.CloudDownloadState;
import com.proxy.service.network.download.notification.NotificationImpl;
import com.proxy.service.utils.info.UtilsFileServiceImpl;

import java.io.File;

/**
 * @author : cangHX
 * on 2020/09/03  8:03 PM
 */
@CloudApiService(serviceTag = CloudServiceTagNetWork.NET_WORK_DOWNLOAD)
public class NetWorkDownloadServiceImpl implements CloudNetWorkDownloadService {

    private String mFileDir;
    private String mFileCacheDir;
    private boolean mNotificationEnable = true;
    private CloudDownloadCallback mGlobalDownloadCallback;
    private CloudNotificationCallback mGlobalNotificationCallback;

    /**
     * 设置最大同时下载数量，默认为 5
     *
     * @param maxCount : 最大数量
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:16 PM
     */
    @NonNull
    @Override
    public CloudNetWorkDownloadService setGlobalMaxConcurrent(int maxCount) {
        DownloadManager.getInstance().setMaxCount(maxCount);
        return this;
    }

    /**
     * 设置下载路径，
     * 可能被具体任务路径替换，如果具体任务设置了路径
     *
     * @param fileDir : 下载路径
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:19 PM
     */
    @NonNull
    @Override
    public CloudNetWorkDownloadService setGlobalDownloadDir(@NonNull String fileDir) {
        this.mFileDir = fileDir;
        return this;
    }

    /**
     * 设置下载缓存路径，
     * 可能被具体任务缓存路径替换，如果具体任务设置了缓存路径
     *
     * @param fileCacheDir : 下载路径
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:19 PM
     */
    @NonNull
    @Override
    public CloudNetWorkDownloadService setGlobalDownloadCacheDir(@NonNull String fileCacheDir) {
        this.mFileCacheDir = fileCacheDir;
        return this;
    }

    /**
     * 设置是否使用多进程进行下载，默认使用
     *
     * @param enable : 是否使用多进程
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:39 PM
     */
    @NonNull
    @Override
    public CloudNetWorkDownloadService setGlobalMultiProcessEnable(boolean enable) {
        DownloadManager.getInstance().setMultiProcessEnable(enable);
        return this;
    }

    /**
     * 设置下载回调，
     * 可能被具体任务回调替换，如果具体任务设置了回调
     *
     * @param callback : 下载回调
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:34 PM
     */
    @NonNull
    @Override
    public CloudNetWorkDownloadService setGlobalDownloadCallback(@NonNull CloudDownloadCallback callback) {
        this.mGlobalDownloadCallback = callback;
        return this;
    }

    /**
     * 设置通知是否显示，默认显示
     * 可能被具体任务设置替换，如果具体任务进行了设置
     *
     * @param enable : 通知是否显示
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:45 PM
     */
    @NonNull
    @Override
    public CloudNetWorkDownloadService setGlobalNotificationEnable(boolean enable) {
        this.mNotificationEnable = enable;
        return this;
    }

    /**
     * 设置 Notification 回调，
     * 可能被具体任务回调替换，如果具体任务设置了回调
     *
     * @param callback : Notification 回调
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:34 PM
     */
    @NonNull
    @Override
    public CloudNetWorkDownloadService setGlobalNotificationCallback(@NonNull CloudNotificationCallback callback) {
        this.mGlobalNotificationCallback = callback;
        return this;
    }

    /**
     * 设置通知构建参数
     *
     * @param info : 通知构建参数
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:04 PM
     */
    @NonNull
    @Override
    public CloudNetWorkDownloadService setGlobalNotificationBuilder(@NonNull CloudNetWorkNotificationInfo info) {
        NotificationImpl.getInstance().setNotificationBuilder(info);
        return this;
    }

    /**
     * 开始下载，小于 0 代表失败
     *
     * @param info : 下载任务参数
     * @return 下载任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:30 PM
     */
    @Override
    public int start(@NonNull CloudNetWorkDownloadInfo info) {
        if (TextUtils.isEmpty(info.getFileUrl())) {
            Logger.Debug("The download missing url");
            return -1;
        }

        CloudNetWorkDownloadInfo.Builder builder = info.build();
        if (info.getDownloadCallback() == null) {
            builder.setDownloadCallback(mGlobalDownloadCallback);
        }
        if (TextUtils.isEmpty(info.getFileDir())) {
            builder.setFilePath(mFileDir);
        }
        if (TextUtils.isEmpty(info.getFileCachePath())) {
            builder.setFileCachePath(mFileCacheDir);
        }
        if (info.getNotificationEnable() == null) {
            builder.setNotificationEnable(mNotificationEnable);
        }
        if (info.getNotificationCallback() == null) {
            builder.setNotificationCallback(mGlobalNotificationCallback);
        }
        builder.checkFilePath();
        info = builder.build();
        DownloadInfo downloadInfo = DbHelper.getInstance().query(TableDownloadInfo.COLUMN_FILE_URL + "=?", new String[]{info.getFileUrl()});
        if (downloadInfo != null) {
            info.setDownloadId(downloadInfo.downloadId);
            DownloadManager.getInstance().start(info);
            return downloadInfo.downloadId;
        }
        downloadInfo = DownloadInfo.getDownloadInfo(info);
        downloadInfo.startTime = System.currentTimeMillis();
        downloadInfo.state = CloudDownloadState.ADD;
        int id = (int) DbHelper.getInstance().insert(downloadInfo);
        if (id >= 0) {
            info.setDownloadId((int) id);
            DownloadManager.getInstance().start(info);
        }
        return id;
    }

    /**
     * 暂停下载
     *
     * @param downloadId : 下载任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:29 PM
     */
    @Override
    public void pause(int downloadId) {
        DownloadManager.getInstance().pause(downloadId);
    }

    /**
     * 恢复下载
     *
     * @param downloadId : 下载任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:29 PM
     */
    @Override
    public void continues(int downloadId) {
        DownloadManager.getInstance().continues(downloadId);
    }

    /**
     * 取消下载
     *
     * @param downloadId : 下载任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:29 PM
     */
    @Override
    public void cancel(int downloadId) {
        DownloadManager.getInstance().cancel(downloadId);
    }

    /**
     * 删除下载，同时删除任务记录
     *
     * @param downloadId : 下载任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:29 PM
     */
    @Override
    public void delete(int downloadId) {
        DownloadManager.getInstance().cancel(downloadId);
        DownloadInfo downloadInfo = DbHelper.getInstance().query(TableDownloadInfo.COLUMN_DOWNLOAD_ID + "=?", new String[]{String.valueOf(downloadId)});
        if (downloadInfo != null) {
            CloudUtilsFileService fileService = new UtilsFileServiceImpl();
            fileService.deleteFile(downloadInfo.fileCachePath);
            fileService.deleteFile(downloadInfo.fileDir + File.separator + downloadInfo.fileName);
            DbHelper.getInstance().delete(TableDownloadInfo.COLUMN_DOWNLOAD_ID + "=?", new String[]{String.valueOf(downloadId)});
        }
    }

    /**
     * 获取下载状态
     * {@link com.proxy.service.api.download.CloudDownloadState}
     *
     * @param downloadId : 下载任务 id
     * @return 下载状态
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:29 PM
     */
    @Override
    public int getDownloadState(int downloadId) {
        DownloadInfo downloadInfo = DbHelper.getInstance().query(TableDownloadInfo.COLUMN_DOWNLOAD_ID + "=?", new String[]{String.valueOf(downloadId)});
        if (downloadInfo != null) {
            return downloadInfo.state;
        }
        return CloudDownloadState.EMPTY;
    }
}
