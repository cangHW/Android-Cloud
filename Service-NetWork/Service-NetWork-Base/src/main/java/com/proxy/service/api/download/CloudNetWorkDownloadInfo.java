package com.proxy.service.api.download;

import android.text.TextUtils;

import com.proxy.service.api.callback.download.CloudDownloadCallback;
import com.proxy.service.api.utils.PathUtils;

import java.io.File;

/**
 * @author : cangHX
 * on 2020/09/02  7:54 PM
 */
public final class CloudNetWorkDownloadInfo {

    public int downloadId = -1;
    public boolean enable = true;

    private String fileDir;
    private String fileCacheFile;
    private String fileName;
    private String fileUrl;
    private String fileMd5;
    private long fileSize;
    private String tag;

    private CloudDownloadCallback downloadCallback;
    private Boolean isNotificationEnable;
    private Boolean isNetworkStateWatchEnable;

    private CloudNetWorkDownloadInfo(Builder builder) {
        if (TextUtils.isEmpty(builder.filePath)) {
            this.fileDir = "";
        } else {
            this.fileDir = PathUtils.getDirFromPath(builder.filePath + File.separator);
        }
        this.fileCacheFile = builder.fileCachePath;
        this.fileName = builder.fileName;
        this.fileUrl = builder.fileUrl;
        this.fileMd5 = builder.fileMd5;
        this.fileSize = builder.fileSize;
        this.tag = builder.tag;

        this.downloadCallback = builder.downloadCallback;
        this.isNotificationEnable = builder.isNotificationEnable;
        this.isNetworkStateWatchEnable = builder.isNetworkStateWatchEnable;
    }

    public String getFileDir() {
        if (TextUtils.isEmpty(fileDir)) {
            return "";
        }
        return fileDir;
    }

    public String getFileCachePath() {
        if (TextUtils.isEmpty(fileCacheFile)) {
            return "";
        }
        return fileCacheFile;
    }

    public String getFileName() {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }
        return fileName;
    }

    public String getFileUrl() {
        if (TextUtils.isEmpty(fileUrl)) {
            return "";
        }
        return fileUrl;
    }

    public String getFileMd5() {
        if (TextUtils.isEmpty(fileMd5)) {
            return "";
        }
        return fileMd5;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getTag() {
        if (TextUtils.isEmpty(tag)) {
            return "";
        }
        return tag;
    }

    public CloudDownloadCallback getDownloadCallback() {
        return downloadCallback;
    }

    public Boolean getNotificationEnable() {
        return isNotificationEnable;
    }

    public Boolean getNetworkStateWatchEnable() {
        return isNetworkStateWatchEnable;
    }

    public Builder build() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String filePath;
        private String fileCachePath;
        private String fileName;
        private String fileUrl;
        private String fileMd5;
        private long fileSize;
        private String tag;

        private CloudDownloadCallback downloadCallback;
        private Boolean isNotificationEnable;
        private Boolean isNetworkStateWatchEnable;

        private Builder(CloudNetWorkDownloadInfo info) {
            this.filePath = info.fileDir;
            this.fileCachePath = info.fileCacheFile;
            this.fileName = info.fileName;
            this.fileUrl = info.fileUrl;
            this.fileMd5 = info.fileMd5;
            this.fileSize = info.fileSize;
            this.tag = info.tag;
            this.downloadCallback = info.downloadCallback;
            this.isNotificationEnable = info.isNotificationEnable;
            this.isNetworkStateWatchEnable = info.isNetworkStateWatchEnable;
        }

        private Builder() {
        }

        public Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder setFileCachePath(String fileCachePath) {
            this.fileCachePath = fileCachePath;
            return this;
        }

        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
            return this;
        }

        public Builder setFileMd5(String fileMd5) {
            this.fileMd5 = fileMd5;
            return this;
        }

        public Builder setFileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder setDownloadCallback(CloudDownloadCallback downloadCallback) {
            this.downloadCallback = downloadCallback;
            return this;
        }

        public Builder setNotificationEnable(boolean notificationEnable) {
            isNotificationEnable = notificationEnable;
            return this;
        }

        public Builder setNetworkStateWatchEnable(boolean networkStateWatchEnable) {
            isNetworkStateWatchEnable = networkStateWatchEnable;
            return this;
        }

        public CloudNetWorkDownloadInfo build() {
            return new CloudNetWorkDownloadInfo(this);
        }

        public void checkFilePath() {
            boolean isFilePathEmpty = TextUtils.isEmpty(filePath);
            boolean isFileCachePathEmpty = TextUtils.isEmpty(fileCachePath);
            boolean isFileNameEmpty = TextUtils.isEmpty(fileName);

            if (isFilePathEmpty) {
                filePath = PathUtils.getDiskFileDir();
            }

            if (isFileNameEmpty) {
                fileName = PathUtils.getNameFromPath(filePath);
                if (TextUtils.isEmpty(fileName)) {
                    fileName = PathUtils.getNameFromPath(fileCachePath);
                }
                if (TextUtils.isEmpty(fileName)) {
                    fileName = PathUtils.CACHE_PREFIX + System.currentTimeMillis();
                }
            }

            filePath = PathUtils.getDirFromPath(filePath);

            if (isFileCachePathEmpty) {
                fileCachePath = filePath + File.separator + fileName + PathUtils.CACHE_SUFFIX;
            }
        }
    }
}
