package com.proxy.service.api.services;

import androidx.annotation.Nullable;

import com.proxy.service.base.BaseService;

import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/11  10:13
 * <p>
 * 分享相关
 */
public interface CloudUtilsShareService extends BaseService {

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
    void shareAppImg(@Nullable List<String> imgPaths);
}
