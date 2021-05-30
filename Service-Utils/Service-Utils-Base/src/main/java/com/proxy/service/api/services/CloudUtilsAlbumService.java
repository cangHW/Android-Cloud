package com.proxy.service.api.services;

import androidx.annotation.NonNull;

import com.proxy.service.api.album.PictureInfo;
import com.proxy.service.base.BaseService;

import java.util.HashMap;
import java.util.List;

/**
 * 相册相关
 *
 * @author : cangHX
 * on 2021/05/25  8:24 PM
 */
public interface CloudUtilsAlbumService extends BaseService {

    interface AlbumForGroupCallback {
        /**
         * 图片加载成功回调
         *
         * @param hashMap : 图片集合
         * @version: 1.0
         * @author: cangHX
         * @date: 2021/5/26 8:51 PM
         */
        void onCall(HashMap<String, List<PictureInfo>> hashMap);

        /**
         * 图片加载失败回调
         *
         * @version: 1.0
         * @author: cangHX
         * @date: 2021/5/26 8:51 PM
         */
        void onFailed();
    }

    interface AlbumCallback {
        /**
         * 图片加载成功回调
         *
         * @param list : 图片集合
         * @version: 1.0
         * @author: cangHX
         * @date: 2021/5/26 8:51 PM
         */
        void onCall(List<PictureInfo> list);

        /**
         * 图片加载失败回调
         *
         * @version: 1.0
         * @author: cangHX
         * @date: 2021/5/26 8:51 PM
         */
        void onFailed();
    }

    /**
     * 获取图片，按时间排序，从新到旧
     *
     * @param callback : 回调
     * @param num      : 获取的数量, 需要大于 0
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/25 9:36 PM
     */
    void findPicture(@NonNull AlbumCallback callback, int num);

    /**
     * 获取全部图片，按时间排序，从新到旧
     *
     * @param callback : 回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/25 9:36 PM
     */
    void findAllPicture(@NonNull AlbumCallback callback);

    /**
     * 以文件夹形式获取图片，按时间排序，从新到旧
     *
     * @param callback : 回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/25 9:36 PM
     */
    void findAllPictureToGroup(@NonNull AlbumForGroupCallback callback);
}
