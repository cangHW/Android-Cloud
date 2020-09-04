package com.proxy.service.api.request.base;

import androidx.annotation.NonNull;

import java.io.File;

/**
 * 网络缓存
 *
 * @author : cangHX
 * on 2020/07/20  9:11 PM
 */
public interface CloudNetWorkCache {

    /**
     * 获取缓存地址
     *
     * @return 缓存地址 file 对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/1 10:10 PM
     */
    @NonNull
    File getDirectory();

    /**
     * 获取最大保存容量
     *
     * @return 最大保存容量
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/1 10:11 PM
     */
    long getMaxSize();
}
