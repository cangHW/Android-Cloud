package com.proxy.service.api.services;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.base.BaseService;

import java.io.File;
import java.util.List;

/**
 * 分享相关
 *
 * @author: cangHX
 * on 2020/06/11  10:13
 */
public interface CloudUtilsShareService extends BaseService {

    /**
     * 获取允许共享的 uri
     *
     * @param file : 文件流
     * @return 允许共享的 uri
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/27 10:18 PM
     */
    @Nullable
    Uri getUriForFile(@Nullable File file);

    /**
     * 打开系统分享，文字
     *
     * @param info : 分享文字内容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:14
     */
    void openSystemShareTxt(@Nullable String info);

    /**
     * 打开系统分享，图片
     *
     * @param imgPaths : 图片地址集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:15
     */
    void openSystemShareImg(@Nullable List<String> imgPaths);
}
