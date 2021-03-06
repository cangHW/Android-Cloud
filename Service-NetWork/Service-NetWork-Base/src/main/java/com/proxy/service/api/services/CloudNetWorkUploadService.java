package com.proxy.service.api.services;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.upload.CloudUploadCallback;
import com.proxy.service.api.upload.CloudNetWorkUploadInfo;
import com.proxy.service.base.BaseService;

/**
 * 网络模块上传服务
 *
 * @author : cangHX
 * on 2020/07/19  6:09 PM
 */
public interface CloudNetWorkUploadService extends BaseService {

    /**
     * 设置最大同时上传数量，默认为 5
     *
     * @param maxCount : 最大数量
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/9 9:15 PM
     */
    @NonNull
    CloudNetWorkUploadService setGlobalMaxConcurrent(int maxCount);

    /**
     * 设置是否使用多进程进行上传，默认使用
     *
     * @param enable : 是否使用多进程
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/9 9:15 PM
     */
    @NonNull
    CloudNetWorkUploadService setGlobalMultiProcessEnable(boolean enable);

    /**
     * 设置上传回调，
     * 可能被具体任务回调替换，如果具体任务设置了回调
     *
     * @param callback : 上传回调
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/9 9:15 PM
     */
    @NonNull
    CloudNetWorkUploadService setGlobalUploadCallback(@NonNull CloudUploadCallback callback);

    /**
     * 开始上传，小于 0 代表失败
     *
     * @param info : 上传任务参数
     * @return 上传任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/9 9:15 PM
     */
    int start(@NonNull CloudNetWorkUploadInfo info);

    /**
     * 暂停上传
     *
     * @param uploadId : 上传任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/9 9:15 PM
     */
    void pause(int uploadId);

    /**
     * 恢复上传
     *
     * @param uploadId : 上传任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/9 9:15 PM
     */
    void continues(int uploadId);

    /**
     * 取消上传
     *
     * @param uploadId : 上传任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/9 9:15 PM
     */
    void cancel(int uploadId);

    /**
     * 删除上传，同时删除任务记录
     *
     * @param uploadId : 上传任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/9 9:15 PM
     */
    void delete(int uploadId);
}
