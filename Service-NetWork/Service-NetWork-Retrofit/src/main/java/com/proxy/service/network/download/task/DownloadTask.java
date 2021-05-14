package com.proxy.service.network.download.task;

import android.text.TextUtils;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.security.SecurityType;
import com.proxy.service.api.services.CloudUtilsFileService;
import com.proxy.service.api.services.CloudUtilsNetWorkService;
import com.proxy.service.api.services.CloudUtilsSecurityService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.network.download.info.DownloadInfo;
import com.proxy.service.network.download.listener.DownloadListener;
import com.proxy.service.network.factory.RetrofitManager;
import com.proxy.service.network.service.RetrofitService;

import java.io.File;
import java.io.FileInputStream;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * @author : cangHX
 * on 2020/09/10  9:34 PM
 */
public class DownloadTask {

    private static final String TAG = "DownloadTask";

    private static final String ERROR_CLASS_MISSING = "依赖库缺失";
    private static final String ERROR_NETWORK = "请检查网络";
    private static final String ERROR_FILE_PER = "请检查文件或者权限";
    private static final String ERROR_REQUEST = "请求出错";
    private static final String ERROR_FILE_MD5 = "文件校验失败";
    private static final String ERROR_FILE = "写文件错误";
    private static final String ERROR_CREATE_FILE = "创建文件失败，请检查权限";

    private static final String WARNING_FILE_TOTAL_LENGTH = "文件总长度未知";
    private static final String WARNING_FILE_MD5 = "缺少文件真实的 md5 值，无法校验文件是否被替换，默认为正常";
    private static final String WARNING_FILE_TOTAL = "文件总长度未知，且本地存在对应文件，默认为已下载过";

    private final CloudUtilsNetWorkService mNetWorkService;
    private final CloudUtilsFileService mFileService;
    private final CloudUtilsSecurityService mSecurityService;
    private DownloadListener mDownloadListener;
    private boolean isCancel = false;

    private DownloadTask() {
        mNetWorkService = CloudSystem.getService(CloudServiceTagUtils.UTILS_NET_WORK);
        mFileService = CloudSystem.getService(CloudServiceTagUtils.UTILS_FILE);
        mSecurityService = CloudSystem.getService(CloudServiceTagUtils.UTILS_SECURITY);
    }

    public static DownloadTask create() {
        return new DownloadTask();
    }

    /**
     * 设置监听
     *
     * @param listener : 下载监听
     */
    public void setDownloadListener(DownloadListener listener) {
        mDownloadListener = listener;
    }

    /**
     * 开始下载
     *
     * @param downloadInfo : 任务信息
     */
    public void start(final DownloadInfo downloadInfo) {
        Logger.Info("开始下载");
        mDownloadListener.onStart(downloadInfo);
        if (mFileService == null || mSecurityService == null) {
            mDownloadListener.onFailed(ERROR_CLASS_MISSING, downloadInfo);
            return;
        }

        if (mNetWorkService != null && !mNetWorkService.isConnected()) {
            mDownloadListener.onFailed(ERROR_NETWORK, downloadInfo);
            return;
        }
        File file = mFileService.createFile(downloadInfo.fileDir + File.separator + downloadInfo.fileName);
        if (file == null) {
            mDownloadListener.onFailed(ERROR_CREATE_FILE, downloadInfo);
            return;
        }
        if (file.isDirectory()) {
            mFileService.deleteFile(file.getAbsolutePath());
            file = mFileService.createFile(downloadInfo.fileDir + File.separator + downloadInfo.fileName);
        }
        if (file == null) {
            mDownloadListener.onFailed(ERROR_CREATE_FILE, downloadInfo);
            return;
        }

        if (!file.isFile()) {
            mDownloadListener.onFailed(ERROR_FILE_PER, downloadInfo);
            return;
        }

        if (checkLocalFile(downloadInfo, file)) {
            Logger.Info("检查本地文件，发现文件已完成");
            mDownloadListener.onSuccess(downloadInfo);
            return;
        }
        File cacheFile = mFileService.createFile(downloadInfo.fileCachePath);
        if (cacheFile == null) {
            mDownloadListener.onFailed(ERROR_CREATE_FILE, downloadInfo);
            return;
        }
        if (cacheFile.isDirectory()) {
            mFileService.deleteFile(cacheFile.getAbsolutePath());
            cacheFile = mFileService.createFile(downloadInfo.fileCachePath);
        }
        if (cacheFile == null) {
            mDownloadListener.onFailed(ERROR_CREATE_FILE, downloadInfo);
            return;
        }

        if (!cacheFile.isFile()) {
            mDownloadListener.onFailed(ERROR_FILE_PER, downloadInfo);
            return;
        }

        long cacheLength = cacheFile.length();

        ResponseBody body = null;
        try {
            Logger.Info("下载请求开始执行");
            Response<ResponseBody> bodyCall = RetrofitManager.getInstance().getRetrofit().create(RetrofitService.class).download(downloadInfo.fileUrl, "bytes=" + cacheLength + "-").execute();
            body = bodyCall.body();
            Logger.Info("下载请求执行完成");
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }

        if (body == null) {
            mDownloadListener.onFailed(ERROR_REQUEST, downloadInfo);
            return;
        }

        if (downloadInfo.fileSize <= 0) {
            downloadInfo.fileSize = cacheLength + body.contentLength();
        }

        if (checkLocalFile(downloadInfo, file)) {
            Logger.Info("IO 任务之前，再次检查本地文件，发现文件已完成");
            mDownloadListener.onSuccess(downloadInfo);
            return;
        }

        Logger.Info("开始写文件");
        mFileService.write(body.byteStream(), cacheFile, cacheLength, new CloudUtilsFileService.ProgressCallback() {
            @Override
            public void onProgress(long progress) {
                mDownloadListener.onProgress(progress, downloadInfo.fileSize, downloadInfo);
            }

            @Override
            public boolean isCancel() {
                return isCancel;
            }
        });
        Logger.Info("写文件结束");

        if (!isCancel) {
            Logger.Info("开始检查本地文件");
            long size = cacheFile.length();
            //最后判断文件完整性,未能成功获取文件总大小按文件完整处理
            if (size > 0 && (size == downloadInfo.fileSize || downloadInfo.fileSize <= 0)) {
                mFileService.write(cacheFile, file);
                if (TextUtils.isEmpty(downloadInfo.fileMd5)) {
                    mDownloadListener.onSuccess(downloadInfo);
                    return;
                }
                try {
                    String md5 = mSecurityService.encode(SecurityType.MD5, new FileInputStream(file));
                    if (md5.equals(downloadInfo.fileMd5)) {
                        mDownloadListener.onSuccess(downloadInfo);
                    } else {
                        mDownloadListener.onFailed(ERROR_FILE_MD5, downloadInfo);
                    }
                } catch (Throwable throwable) {
                    Logger.Debug(throwable);
                }
                return;
            }
            Logger.Info("检查本地文件结束");
            mFileService.deleteFile(downloadInfo.fileCachePath);
            mDownloadListener.onFailed(ERROR_FILE, downloadInfo);
        }
    }

    /**
     * 取消任务
     */
    public void cancel() {
        Logger.Info("任务取消");
        this.isCancel = true;
    }

    private boolean checkLocalFile(DownloadInfo info, File localFile) {
        if (localFile.length() > 0) {
            mFileService.deleteFile(info.fileCachePath);
        }

        if (info.fileSize <= 0) {
            mDownloadListener.onWarning(WARNING_FILE_TOTAL_LENGTH, info);
        }

        if (info.fileSize <= 0 && localFile.length() > 0) {
            mDownloadListener.onWarning(WARNING_FILE_TOTAL, info);
            return true;
        }

        if (info.fileSize > 0 && localFile.length() != info.fileSize) {
            mFileService.deleteFile(localFile.getAbsolutePath());
            return false;
        }

        if (info.fileSize > 0 && localFile.length() == info.fileSize) {
            try {
                if (TextUtils.isEmpty(info.fileMd5)) {
                    mDownloadListener.onWarning(WARNING_FILE_MD5, info);
                    return true;
                }
                String md5 = mSecurityService.encode(SecurityType.MD5, new FileInputStream(localFile));
                if (md5.equals(info.fileMd5)) {
                    return true;
                }
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
            mFileService.deleteFile(localFile.getAbsolutePath());
            return false;
        }
        return false;
    }
}
