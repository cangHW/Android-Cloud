package com.proxy.service.api.services;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.proxy.service.base.BaseService;

/**
 * 系统页面相关
 *
 * @author: cangHX
 * on 2020/06/18  13:47
 */
public interface CloudUtilsSystemService extends BaseService {

    /**
     * 打开应用设置页面
     *
     * @param packageName : 包名，为空默认使用当前app的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:11
     */
    void openAppSetting(@Nullable String packageName);

    /**
     * 打开应用通知设置页面
     *
     * @param packageName : 包名，为空默认使用当前app的数据
     * @param uid         : 应用的uid，为空默认使用当前app的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:11
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    void openNotificationSetting(@Nullable String packageName, @Nullable String uid);

    /**
     * 打电话
     *
     * @param phoneNumber : 准备拨打的电话号码
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-18 14:26
     */
    void openCall(@Nullable String phoneNumber);

    /**
     * 展示文件在相册中，会把文件复制到相册对应的文件夹下面
     *
     * @param folderName : 文件夹名称，可以为空
     * @param fileName   : 文件名称，可以为空，默认从路径中获取
     * @param mimeType   : 文件格式，可以为空
     * @param filePath   : 文件路径，不能为空
     * @return: 是否成功
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/12/19 22:14
     */
    boolean showFileInAlbum(@Nullable String folderName, @Nullable String fileName, @Nullable String mimeType, @NonNull String filePath);

}
