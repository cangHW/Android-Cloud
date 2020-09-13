package com.proxy.service.api.services;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.download.CloudDownloadCallback;
import com.proxy.service.api.download.CloudNetWorkDownloadInfo;
import com.proxy.service.api.download.CloudNetWorkNotificationInfo;
import com.proxy.service.base.BaseService;

/**
 * 网络模块下载服务
 *
 * @author : cangHX
 * on 2020/07/19  7:08 PM
 */
public interface CloudNetWorkDownloadService extends BaseService {

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
    CloudNetWorkDownloadService setGlobalMaxConcurrent(int maxCount);

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
    CloudNetWorkDownloadService setGlobalDownloadDir(@NonNull String fileDir);

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
    CloudNetWorkDownloadService setGlobalDownloadCacheDir(@NonNull String fileCacheDir);

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
    CloudNetWorkDownloadService setGlobalMultiProcessEnable(boolean enable);

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
    CloudNetWorkDownloadService setGlobalDownloadCallback(@NonNull CloudDownloadCallback callback);

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
    CloudNetWorkDownloadService setGlobalNotificationEnable(boolean enable);

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
    CloudNetWorkDownloadService setGlobalNotification(@NonNull CloudNetWorkNotificationInfo info);

    /**
     * 设置是否观察网络状态，自动更改下载状态，默认不观察
     *
     * @param enable : 是否观察网络状态，自动更改下载状态
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 6:38 PM
     */
    @NonNull
    CloudNetWorkDownloadService setGlobalNetworkStateWatchEnable(boolean enable);

    /**
     * 开始下载，小于 0 代表失败
     *
     * @param info : 下载任务参数
     * @return 下载任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:30 PM
     */
    long start(@NonNull CloudNetWorkDownloadInfo info);

    /**
     * 暂停下载
     *
     * @param downloadId : 下载任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:29 PM
     */
    void pause(int downloadId);

    /**
     * 恢复下载
     *
     * @param downloadId : 下载任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:29 PM
     */
    void continues(int downloadId);

    /**
     * 取消下载
     *
     * @param downloadId : 下载任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:29 PM
     */
    void cancel(int downloadId);

    /**
     * 删除下载，同时删除任务记录
     *
     * @param downloadId : 下载任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/2 7:29 PM
     */
    void delete(int downloadId);
}
