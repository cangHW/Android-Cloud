package com.proxy.service.api.services;

import androidx.annotation.Nullable;

import com.proxy.service.base.BaseService;

import java.io.File;
import java.io.InputStream;

/**
 * @author : cangHX
 * on 2020/09/08  9:15 PM
 */
public interface CloudUtilsFileService extends BaseService {

    interface ProgressCallback {
        /**
         * 进度回调
         *
         * @param progress : 进度
         */
        void onProgress(long progress);

        /**
         * 是否取消
         */
        boolean isCancel();
    }

    /**
     * 创建 file，自动创建相关文件夹与文件
     *
     * @param path : 文件地址
     * @return 创建好的 file 对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/10 11:03 PM
     */
    @Nullable
    File createFile(String path);

    /**
     * 删除文件
     *
     * @param path : 文件地址
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/10 11:03 PM
     */
    void deleteFile(String path);

    /**
     * 写文件，同步执行
     *
     * @param oldFile : 旧位置或旧名称
     * @param newFile : 新位置或新名称
     * @return true 成功，false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/8 9:16 PM
     */
    boolean write(File oldFile, File newFile);

    /**
     * 写文件，同步执行
     *
     * @param is        : 文件流
     * @param localFile : 本地文件
     * @param seek      : 偏移位置
     * @param callback  : 进度回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/10 10:47 PM
     */
    void write(InputStream is, File localFile, long seek, ProgressCallback callback);
}
