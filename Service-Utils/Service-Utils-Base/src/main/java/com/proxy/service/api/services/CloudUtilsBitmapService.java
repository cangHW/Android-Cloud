package com.proxy.service.api.services;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.proxy.service.base.BaseService;

/**
 * bitmap 相关
 *
 * @author : cangHX
 * on 2020/09/17  10:18 PM
 */
public interface CloudUtilsBitmapService extends BaseService {

    /**
     * Drawable 转 bitmap
     *
     * @param drawable : drawable
     * @return 转换好的bitmap
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/17 10:19 PM
     */
    @Nullable
    Bitmap toBitmap(Drawable drawable);

    /**
     * Drawable 转 bitmap
     *
     * @param drawableId : drawable Id
     * @return 转换好的bitmap
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/17 10:19 PM
     */
    @Nullable
    Bitmap toBitmap(@DrawableRes int drawableId);

}
