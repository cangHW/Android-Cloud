package com.proxy.service.api.bitmap;

import android.graphics.Bitmap;

/**
 * @author : cangHX
 * on 2020/09/23  11:06 PM
 */
public class CloudCaptchaInfo {

    private final Bitmap bitmap;
    private final String key;

    public CloudCaptchaInfo(Bitmap bitmap, String key) {
        this.bitmap = bitmap;
        this.key = key;
    }

    /**
     * 获取验证码图片
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * 获取验证码内容
     */
    public String getKey() {
        return key;
    }
}
