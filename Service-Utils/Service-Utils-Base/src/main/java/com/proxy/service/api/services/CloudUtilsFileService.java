package com.proxy.service.api.services;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.base.BaseService;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 文件 I/O 相关
 *
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
     * 添加允许通过 provider 共享的文件路径，用于获取资源 Uri 等
     * 如果不设置，默认所有路径都是安全路径，建议设置
     *
     * @param filePath : 允许共享的安全路径
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-19 13:30
     */
    void addProviderResourcePath(@NonNull String filePath);

    /**
     * 获取 uri
     *
     * @param file : 文件流
     * @return uri
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/27 10:18 PM
     */
    @Nullable
    Uri getUriForFile(@NonNull File file);

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
    boolean deleteFile(String path);

    /**
     * 读文件，同步执行
     *
     * @param file : 文件位置
     * @return 文件全部内容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/8 9:16 PM
     */
    @NonNull
    String read(File file);

    /**
     * 读文件，同步执行
     *
     * @param file : 文件位置
     * @return 文件每行内容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/8 9:16 PM
     */
    @NonNull
    List<String> readLines(File file);

    /**
     * 写文件，同步执行
     *
     * @param data   : 内容
     * @param file   : 文件位置
     * @param append : 是否续写
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/8 9:16 PM
     */
    boolean write(File file, String data, boolean append);

    /**
     * 写文件，同步执行
     *
     * @param datas  : 内容
     * @param file   : 文件位置
     * @param append : 是否续写
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/8 9:16 PM
     */
    boolean writeLines(File file, List<String> datas, boolean append);

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
    boolean write(InputStream is, File localFile, long seek, ProgressCallback callback);
}
