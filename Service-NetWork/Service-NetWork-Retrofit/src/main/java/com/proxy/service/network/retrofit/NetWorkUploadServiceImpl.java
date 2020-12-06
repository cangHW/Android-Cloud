package com.proxy.service.network.retrofit;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.callback.upload.CloudUploadCallback;
import com.proxy.service.api.services.CloudNetWorkUploadService;
import com.proxy.service.api.tag.CloudServiceTagNetWork;
import com.proxy.service.api.upload.CloudNetWorkUploadInfo;
import com.proxy.service.api.utils.Logger;

/**
 * @author : cangHX
 * on 2020/11/09  9:34 PM
 */
@CloudApiService(serviceTag = CloudServiceTagNetWork.NET_WORK_UPLOAD)
public class NetWorkUploadServiceImpl implements CloudNetWorkUploadService {

    private CloudUploadCallback mGlobalUploadCallback;

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
    @Override
    public CloudNetWorkUploadService setGlobalMaxConcurrent(int maxCount) {
        return this;
    }

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
    @Override
    public CloudNetWorkUploadService setGlobalMultiProcessEnable(boolean enable) {
        return this;
    }

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
    @Override
    public CloudNetWorkUploadService setGlobalUploadCallback(@NonNull CloudUploadCallback callback) {
        this.mGlobalUploadCallback = callback;
        return this;
    }

    /**
     * 开始上传，小于 0 代表失败
     *
     * @param info : 上传任务参数
     * @return 上传任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/9 9:15 PM
     */
    @Override
    public int start(@NonNull CloudNetWorkUploadInfo info) {
        if (TextUtils.isEmpty(info.getUploadUrl())) {
            Logger.Debug("The upload missing url");
            return -1;
        }
        return -1;
    }

    /**
     * 暂停上传
     *
     * @param uploadId : 上传任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/9 9:15 PM
     */
    @Override
    public void pause(int uploadId) {

    }

    /**
     * 恢复上传
     *
     * @param uploadId : 上传任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/9 9:15 PM
     */
    @Override
    public void continues(int uploadId) {

    }

    /**
     * 取消上传
     *
     * @param uploadId : 上传任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/9 9:15 PM
     */
    @Override
    public void cancel(int uploadId) {

    }

    /**
     * 删除上传，同时删除任务记录
     *
     * @param uploadId : 上传任务 id
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/11/9 9:15 PM
     */
    @Override
    public void delete(int uploadId) {

    }
}
