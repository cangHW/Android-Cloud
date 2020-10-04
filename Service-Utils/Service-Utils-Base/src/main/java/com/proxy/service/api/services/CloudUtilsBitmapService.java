package com.proxy.service.api.services;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.bitmap.CloudCaptchaInfo;
import com.proxy.service.api.bitmap.CloudPrintWordMessage;
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
    Bitmap toBitmap(@NonNull Drawable drawable);

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

    /**
     * 压缩图片,尺寸压缩
     *
     * @param bitmap    : 准备压缩的bitmap
     * @param maxWidth  : 最大宽度
     * @param maxHeight : 最大高度
     * @param isAdjust  : 是否自动调整尺寸, true 图片按比例自动调整，false 严格按照尺寸压缩
     * @return 压缩后的Bitmap
     * @version: 1.0
     * @author: cangHX
     */
    Bitmap compressBitmapBySize(@NonNull Bitmap bitmap, int maxWidth, int maxHeight, boolean isAdjust);

    /**
     * 压缩图片,质量压缩 适用于图片上传
     *
     * @param bitmap : 准备压缩的bitmap
     * @param maxKb  : 图片最大保留多少 kb
     * @return 返回压缩后的bitmap
     * @version: 1.0
     * @author: cangHX
     */
    Bitmap compressBitmapByQuality(@NonNull Bitmap bitmap, int maxKb);

    /**
     * 旋转图片
     *
     * @param bitmap  : 准备旋转的bitmap
     * @param degrees : 旋转角度(90为顺时针旋转,-90为逆时针旋转)
     * @return 旋转后的Bitmap
     * @version: 1.0
     * @author: cangHX
     */
    Bitmap rotate(@NonNull Bitmap bitmap, float degrees);

    /**
     * 放大或缩小图片
     *
     * @param bitmap : 准备放大或缩小的bitmap
     * @param ratio  : 放大或缩小的倍数，大于1表示放大，小于1表示缩小
     * @return 处理后的Bitmap
     * @version: 1.0
     * @author: cangHX
     */
    Bitmap zoom(@NonNull Bitmap bitmap, float ratio);

    /**
     * 在图片上印字
     *
     * @param bitmap  : 需要印文字的bitmap
     * @param text    : 需要印上去的文字
     * @param message : 字体信息
     * @return 修改后的Bitmap
     * @version: 1.0
     * @author: cangHX
     */
    Bitmap printWord(@NonNull Bitmap bitmap, @NonNull String text, @Nullable CloudPrintWordMessage message);

    /**
     * 图片重叠绘制，可以用于给图片加水印等
     *
     * @param src  : 源图片
     * @param dst  : 准备合并绘制的图片
     * @param left : 左边起点坐标
     * @param top  : 顶部起点坐标
     * @return 合成后的Bitmap
     */
    Bitmap overlap(@NonNull Bitmap src, @NonNull Bitmap dst, int left, int top);

    /**
     * 创建图形验证码（默认 4 位数字与字母）
     *
     * @param width    : 图片宽度(如果小于等于0，则使用默认值：200)
     * @param height   : 图片高度(如果小于等于0，则使用默认值：80)
     * @param keyCode  : 验证码内容(如果为空，则自动生成随机内容)
     * @param textSize : 文字大小(如果小于等于0，则使用默认值：30)
     * @return 图形验证码
     */
    CloudCaptchaInfo captcha(int width, int height, String keyCode, float textSize);
}
