package com.proxy.service.api.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.base.BaseService;

import java.util.List;

/**
 * 分享相关
 *
 * @author: cangHX
 * on 2020/06/11  10:13
 */
public interface CloudUtilsShareService extends BaseService {

    /**
     * 打开系统分享，文字
     *
     * @param info  : 分享文字内容
     * @param title : 分享的标题
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:14
     */
    void openSystemShareTxt(@NonNull String info, @Nullable String title);

    /**
     * 打开系统分享，图片
     *
     * @param imgPath : 图片地址
     * @param title   : 分享的标题
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:15
     */
    void openSystemShareImg(@NonNull String imgPath, @Nullable String title);

    /**
     * 打开系统分享，图片
     *
     * @param imgPaths : 图片地址集合
     * @param title    : 分享的标题
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:15
     */
    void openSystemShareImg(@NonNull List<String> imgPaths, @Nullable String title);
}
