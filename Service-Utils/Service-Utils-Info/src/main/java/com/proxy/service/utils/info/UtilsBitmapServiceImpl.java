package com.proxy.service.utils.info;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.Nullable;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.services.CloudUtilsBitmapService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;

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
     * @return 转换好的bitmap
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/17 10:19 PM
     */
    @Override
    public Bitmap toBitmap(Drawable drawable) {
        if (drawable == null) {
            Logger.Debug("Please check the Drawable with : " + drawable);
            return null;
        }
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
            int opacity = drawable.getOpacity();
            Bitmap.Config config = opacity != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;

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
     * @return 转换好的bitmap
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/17 10:19 PM
     */
    @Nullable
    @Override
    public Bitmap toBitmap(int drawableId) {
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
        return toBitmap(drawable);
    }
}
