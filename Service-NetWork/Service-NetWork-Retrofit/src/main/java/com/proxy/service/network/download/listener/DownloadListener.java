package com.proxy.service.network.download.listener;

import com.proxy.service.network.download.info.DownloadInfo;

/**
 * @author : cangHX
 * on 2020/09/10  9:42 PM
 */
public interface DownloadListener {

    /**
     * 开始下载
     *
     * @param info : 任务信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/10 9:47 PM
     */
    void onStart(DownloadInfo info);

    /**
     * 下载中
     *
     * @param progress : 进度
     * @param total    : 文件总大小
     * @param info     : 任务信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/10 9:47 PM
     */
    void onProgress(long progress, long total, DownloadInfo info);

    /**
     * 下载成功
     *
     * @param info : 任务信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/10 9:47 PM
     */
    void onSuccess(DownloadInfo info);

    /**
     * 出现警告
     *
     * @param warningMsg : 警告信息
     * @param info       : 任务信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/10 9:49 AM
     */
    void onWarning(String warningMsg, DownloadInfo info);

    /**
     * 下载失败
     *
     * @param errorMsg : 失败信息
     * @param info     : 任务信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/10 9:47 PM
     */
    void onFailed(String errorMsg, DownloadInfo info);

}
