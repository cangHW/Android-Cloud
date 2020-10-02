package com.proxy.service.api.download;

import com.proxy.service.api.callback.download.CloudDownloadCallback;

/**
 * @author : cangHX
 * on 2020/09/28  10:16 PM
 */
public class DownloadCallbackEmptyImpl implements CloudDownloadCallback {
    /**
     * 开始下载
     *
     * @param info : 任务信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/6 4:40 PM
     */
    @Override
    public void onStart(CloudNetWorkDownloadInfo info) {

    }

    /**
     * 下载中
     *
     * @param info     : 任务信息
     * @param progress : 下载进度
     * @param total    : 文件总大小
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/6 4:40 PM
     */
    @Override
    public void onProgress(CloudNetWorkDownloadInfo info, long progress, long total) {

    }

    /**
     * 暂停下载
     *
     * @param info : 任务信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/6 4:40 PM
     */
    @Override
    public void onPause(CloudNetWorkDownloadInfo info) {

    }

    /**
     * 继续下载
     *
     * @param info : 任务信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/6 4:40 PM
     */
    @Override
    public void onContinue(CloudNetWorkDownloadInfo info) {

    }

    /**
     * 出现警告
     *
     * @param errorMsg : 警告信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/6 4:44 PM
     */
    @Override
    public void onWarning(String errorMsg) {

    }

    /**
     * 下载成功
     *
     * @param info : 任务信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/6 4:40 PM
     */
    @Override
    public void onSuccess(CloudNetWorkDownloadInfo info) {

    }

    /**
     * 下载失败
     *
     * @param errorMsg : 错误信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/6 4:40 PM
     */
    @Override
    public void onFailed(String errorMsg) {

    }
}
