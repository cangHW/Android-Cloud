package com.proxy.service.utils.info;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.bitmap.CloudCaptchaInfo;
import com.proxy.service.api.bitmap.CloudPrintWordMessage;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsBitmapService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.bitmap.CaptchaBitmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

/**
 * @author : cangHX
 * on 2020/09/17  10:21 PM
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_BITMAP)
public class UtilsBitmapServiceImpl implements CloudUtilsBitmapService {
    /**
     * Drawable 转 bitmap
     *
     * @param drawable : 原始数据
     * @param config   : config
     * @return 转换好的bitmap
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/17 10:19 PM
     */
    @Override
    public Bitmap toBitmap(@NonNull Drawable drawable, @Nullable Bitmap.Config config) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return bitmapDrawable.getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        if (width <= 0 || height <= 0) {
            return null;
        }

        Bitmap bitmap = null;
        try {
            if (config == null) {
                config = Bitmap.Config.ARGB_8888;
            }
            bitmap = Bitmap.createBitmap(width, height, config);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }

        return bitmap;
    }

    /**
     * Drawable 转 bitmap
     *
     * @param drawableId : drawable Id
     * @param config     : config
     * @return 转换好的bitmap
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/17 10:19 PM
     */
    @Nullable
    @Override
    public Bitmap toBitmap(int drawableId, @Nullable Bitmap.Config config) {
        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return null;
        }
        Drawable drawable = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                drawable = context.getResources().getDrawable(drawableId, null);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        } else {
            try {
                drawable = context.getResources().getDrawable(drawableId);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
        if (drawable == null) {
            Logger.Debug("Please check the DrawableRes with : " + drawableId);
            return null;
        }
        return toBitmap(drawable, config);
    }

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
    @Override
    public Bitmap compressBitmapBySize(@NonNull Bitmap bitmap, int maxWidth, int maxHeight, boolean isAdjust) {
        if (maxWidth <= 0 || maxHeight <= 0) {

            return bitmap;
        }
        if (bitmap.getWidth() < maxWidth && bitmap.getHeight() < maxHeight) {
            return bitmap;
        }
        float sx = new BigDecimal(String.valueOf(maxWidth)).divide(new BigDecimal(String.valueOf(bitmap.getWidth())), 4, BigDecimal.ROUND_DOWN).floatValue();
        float sy = new BigDecimal(String.valueOf(maxHeight)).divide(new BigDecimal(String.valueOf(bitmap.getHeight())), 4, BigDecimal.ROUND_DOWN).floatValue();
        if (isAdjust) {
            float max = Math.max(sx, sy);
            sx = sy = max;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 压缩图片,质量压缩 适用于图片上传
     *
     * @param bitmap : 准备压缩的bitmap
     * @param maxKb  : 图片最大保留多少 kb
     * @return 返回压缩后的bitmap
     * @version: 1.0
     * @author: cangHX
     */
    @Override
    public Bitmap compressBitmapByQuality(@NonNull Bitmap bitmap, int maxKb) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > maxKb) {
            baos.reset();
            options -= 5;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    /**
     * 旋转图片
     *
     * @param bitmap  : 准备旋转的bitmap
     * @param degrees : 旋转角度(90为顺时针旋转,-90为逆时针旋转)
     * @return 旋转后的Bitmap
     * @version: 1.0
     * @author: cangHX
     */
    @Override
    public Bitmap rotate(@NonNull Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 放大或缩小图片
     *
     * @param bitmap : 准备放大或缩小的bitmap
     * @param ratio  : 放大或缩小的倍数，大于1表示放大，小于1表示缩小
     * @return 处理后的Bitmap
     * @version: 1.0
     * @author: cangHX
     */
    @Override
    public Bitmap zoom(@NonNull Bitmap bitmap, float ratio) {
        if (ratio < 0f) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

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
    @Override
    public Bitmap printWord(@NonNull Bitmap bitmap, @NonNull String text, @Nullable CloudPrintWordMessage message) {
        if (TextUtils.isEmpty(text)) {
            return bitmap;
        }

        if (message == null) {
            message = CloudPrintWordMessage.builder().build();
        }

        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        newBitmap.setDensity(bitmap.getDensity());
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(message.getStrokeWidth());
        paint.setColor(message.getTextColor());
        paint.setTextSize(message.getTextSize());
        paint.setFakeBoldText(message.isBold());
        float x = message.getLeft();
        float y = message.getTop();
        if (x < 0) {
            paint.setTextAlign(Paint.Align.CENTER);
            x = bitmap.getWidth() >> 1;
        }
        if (y < 0) {
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
            y = (bitmap.getHeight() >> 1) + distance;
        }
        canvas.drawText(text, x, y, paint);
        return newBitmap;
    }

    /**
     * 图片重叠绘制，可以用于给图片加水印等
     *
     * @param src  : 源图片
     * @param dst  : 准备合并绘制的图片
     * @param left : 左边起点坐标
     * @param top  : 顶部起点坐标
     * @return 合成后的Bitmap
     */
    @Override
    public Bitmap overlap(@NonNull Bitmap src, @NonNull Bitmap dst, int left, int top) {
        Bitmap newBitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        newBitmap.setDensity(src.getDensity());
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(src, 0, 0, null);
        canvas.drawBitmap(dst, left, top, null);
        return newBitmap;
    }

    /**
     * 创建图形验证码（默认 4 位数字与字母）
     *
     * @param width    : 图片宽度(如果小于等于0，则使用默认值：200)
     * @param height   : 图片高度(如果小于等于0，则使用默认值：80)
     * @param keyCode  : 验证码内容(如果为空，则自动生成随机内容)
     * @param textSize : 文字大小(如果小于等于0，则使用默认值：30)
     * @return 图形验证码
     */
    @Override
    public CloudCaptchaInfo captcha(int width, int height, String keyCode, float textSize) {
        CaptchaBitmap captchaBitmap = CaptchaBitmap.create();
        if (width <= 0) {
            width = 200;
        }
        if (height <= 0) {
            height = 80;
        }
        if (textSize <= 0) {
            height = 30;
        }
        if (TextUtils.isEmpty(keyCode)) {
            keyCode = captchaBitmap.createKeyCode();
        }
        return new CloudCaptchaInfo(captchaBitmap.createBitmap(width, height, keyCode, textSize), captchaBitmap.getKeyCode());
    }

}
