package com.proxy.service.api.download;

import android.text.TextUtils;

import com.proxy.service.api.callback.download.CloudDownloadCallback;
import com.proxy.service.api.callback.download.CloudNotificationCallback;
import com.proxy.service.api.utils.PathUtils;

/**
 * @author : cangHX
 * on 2020/09/02  7:54 PM
 */
public final class CloudNetWorkDownloadInfo {

    public static final int PROGRESS_EMPTY = -100;

    private int downloadId = -1;
    private boolean isPause = false;

    private final String taskName;

    private final String fileDir;
    private final String fileCachePath;
    private final String fileName;
    private final String fileUrl;
    private final String fileMd5;
    private final long fileSize;
    private final String tag;

    private CloudDownloadCallback downloadCallback;
    private CloudNotificationCallback notificationCallback;
    private final Boolean isNotificationEnable;

    private int progress = PROGRESS_EMPTY;

    private CloudNetWorkDownloadInfo(Builder builder) {
        this.taskName = builder.taskName;
        if (TextUtils.isEmpty(builder.filePath)) {
            this.fileDir = "";
        } else {
            this.fileDir = PathUtils.getDirFromPath(builder.filePath);
        }
        this.fileCachePath = builder.fileCachePath;
        this.fileName = builder.fileName;
        this.fileUrl = builder.fileUrl;
        this.fileMd5 = builder.fileMd5;
        this.fileSize = builder.fileSize;
        this.tag = builder.tag;

        this.downloadCallback = builder.downloadCallback;
        this.notificationCallback = builder.notificationCallback;
        this.isNotificationEnable = builder.isNotificationEnable;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public boolean isPause() {
        return isPause;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getTaskName() {
        if (TextUtils.isEmpty(taskName)) {
            return getFileName();
        }
        return taskName;
    }

    public String getFileDir() {
        if (TextUtils.isEmpty(fileDir)) {
            return "";
        }
        return fileDir;
    }

    public String getFileCachePath() {
        if (TextUtils.isEmpty(fileCachePath)) {
            return "";
        }
        return fileCachePath;
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
        if (downloadCallback == null) {
            downloadCallback = new DownloadCallbackEmptyImpl();
        }
        return downloadCallback;
    }

    public CloudNotificationCallback getNotificationCallback() {
        if (notificationCallback == null) {
            notificationCallback = new NotificationCallbackEmptyImpl();
        }
        return notificationCallback;
    }

    public Boolean getNotificationEnable() {
        return isNotificationEnable;
    }

    public Builder build() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String taskName;

        private String filePath;
        private String fileCachePath;
        private String fileName;
        private String fileUrl;
        private String fileMd5;
        private long fileSize;
        private String tag;

        private CloudDownloadCallback downloadCallback;
        private CloudNotificationCallback notificationCallback;
        private Boolean isNotificationEnable;

        private Builder(CloudNetWorkDownloadInfo info) {
            this.taskName = info.taskName;
            this.filePath = info.fileDir;
            this.fileCachePath = info.fileCachePath;
            this.fileName = info.fileName;
            this.fileUrl = info.fileUrl;
            this.fileMd5 = info.fileMd5;
            this.fileSize = info.fileSize;
            this.tag = info.tag;
            this.downloadCallback = info.downloadCallback;
            this.isNotificationEnable = info.isNotificationEnable;
            this.notificationCallback = info.notificationCallback;
        }

        private Builder() {
        }

        /**
         * 设置任务名称(为空则使用文件名称)
         */
        public Builder setTaskName(String taskName) {
            this.taskName = taskName;
            return this;
        }

        /**
         * 设置文件路径(为空则自动创建)
         */
        public Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        /**
         * 设置缓存路径(为空则自动创建)
         */
        public Builder setFileCachePath(String fileCachePath) {
            this.fileCachePath = fileCachePath;
            return this;
        }

        /**
         * 设置文件名称(为空则从下载地址获取或自动创建)
         */
        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        /**
         * 设置下载地址(不能为空)
         */
        public Builder setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
            return this;
        }

        /**
         * 设置文件 md5 值(为空则不校验 md5)
         */
        public Builder setFileMd5(String fileMd5) {
            this.fileMd5 = fileMd5;
            return this;
        }

        /**
         * 设置文件大小(为空则从 sever 获取)
         */
        public Builder setFileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        /**
         * 设置任务 tag
         */
        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        /**
         * 设置下载回调(为空则使用全局下载回调)
         */
        public Builder setDownloadCallback(CloudDownloadCallback downloadCallback) {
            this.downloadCallback = downloadCallback;
            return this;
        }

        /**
         * 设置是否显示通知(为空则使用全局设置，默认显示)
         */
        public Builder setNotificationEnable(boolean notificationEnable) {
            isNotificationEnable = notificationEnable;
            return this;
        }

        /**
         * 设置通知回调(为空则使用全局通知回调)，弱引用，注意数据回收，回收后无法接收回调
         */
        public Builder setNotificationCallback(CloudNotificationCallback notificationCallback) {
            this.notificationCallback = notificationCallback;
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
                    fileName = PathUtils.getNameFromPath(fileUrl);
                }
                if (TextUtils.isEmpty(fileName)) {
                    fileName = PathUtils.CACHE_PREFIX + System.currentTimeMillis();
                }
            }

            filePath = PathUtils.getDirFromPath(filePath);

            if (isFileCachePathEmpty) {
                fileCachePath = filePath + fileName + PathUtils.CACHE_SUFFIX;
            }
        }
    }
}
