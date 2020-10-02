package com.proxy.service.network.utils;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.download.CloudNetWorkDownloadInfo;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsFileService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.api.utils.PathUtils;
import com.proxy.service.network.download.info.DownloadInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/09/04  7:26 PM
 */
public class DownloadInfoUtils {

    public interface CheckedCallback {
        /**
         * 发现符合要求的数据对象
         *
         * @param info : 符合要求的数据对象
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/9/4 7:28 PM
         */
        void onFound(CloudNetWorkDownloadInfo info);
    }

    public static void findValueById(List<CloudNetWorkDownloadInfo> list, int downloadId, CheckedCallback callback) {
        for (CloudNetWorkDownloadInfo info : new ArrayList<>(list)) {
            if (info.getDownloadId() != downloadId) {
                continue;
            }
            callback.onFound(info);
        }
    }

    public static boolean isHasValue(List<CloudNetWorkDownloadInfo> list, int downloadId) {
        for (CloudNetWorkDownloadInfo downloadInfo : new ArrayList<>(list)) {
            if (downloadInfo.getDownloadId() != downloadId) {
                continue;
            }
            return true;
        }
        return false;
    }

    public static DownloadInfo compare(CloudNetWorkDownloadInfo downloadInfo, DownloadInfo dbDownloadInfo) {
        if (dbDownloadInfo == null) {
            return DownloadInfo.getDownloadInfo(downloadInfo);
        }
        DownloadInfo info = null;
        CloudUtilsFileService service = CloudSystem.getService(CloudServiceTagUtils.UTILS_FILE);
        if (service == null) {
            Logger.Debug(CloudApiError.UNKNOWN_ERROR.setMsg("Please check whether to use \"exclude\" to remove partial dependencies").build());
            return null;
        }
        if (!downloadInfo.getFileName().equals(dbDownloadInfo.fileName)) {
            if (!downloadInfo.getFileName().startsWith(PathUtils.CACHE_PREFIX)) {
                service.write(new File(dbDownloadInfo.fileDir, dbDownloadInfo.fileName), new File(dbDownloadInfo.fileDir, downloadInfo.getFileName()));
                dbDownloadInfo.fileName = downloadInfo.getFileName();
                info = dbDownloadInfo;
            }
        }
        if (!downloadInfo.getFileDir().equals(dbDownloadInfo.fileDir)) {
            service.write(new File(dbDownloadInfo.fileDir, dbDownloadInfo.fileName), new File(downloadInfo.getFileDir(), downloadInfo.getFileName()));
            dbDownloadInfo.fileDir = downloadInfo.getFileDir();
            info = dbDownloadInfo;
        }
        if (!downloadInfo.getFileCachePath().equals(dbDownloadInfo.fileCachePath)) {
            service.write(new File(dbDownloadInfo.fileCachePath), new File(downloadInfo.getFileCachePath()));
            dbDownloadInfo.fileCachePath = downloadInfo.getFileCachePath();
            info = dbDownloadInfo;
        }
        if (!downloadInfo.getFileMd5().equals(dbDownloadInfo.fileMd5)) {
            dbDownloadInfo.fileMd5 = downloadInfo.getFileMd5();
            info = dbDownloadInfo;
        }

        if (downloadInfo.getFileSize() != dbDownloadInfo.fileSize) {
            if (downloadInfo.getFileSize() != 0) {
                dbDownloadInfo.fileSize = downloadInfo.getFileSize();
                info = dbDownloadInfo;
            }
        }
        if (!downloadInfo.getTag().equals(dbDownloadInfo.tag)) {
            dbDownloadInfo.tag = downloadInfo.getTag();
            info = dbDownloadInfo;
        }
        if (info != null) {
            info.downloadId = dbDownloadInfo.downloadId;
        }
        return info;
    }

}
