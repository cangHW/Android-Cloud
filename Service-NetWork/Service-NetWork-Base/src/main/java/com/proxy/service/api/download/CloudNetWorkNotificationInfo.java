package com.proxy.service.api.download;

import android.graphics.Bitmap;

import androidx.annotation.DrawableRes;

/**
 * @author : cangHX
 * on 2020/09/02  8:03 PM
 */
public final class CloudNetWorkNotificationInfo {

    public enum ChannelLevel {
        /**
         * 完全不重要，通知允许被折叠
         */
        NONE,
        /**
         * 稍微重要，通知允许被折叠
         */
        MIN,
        /**
         * 重要程度--低
         */
        LOW,
        /**
         * 重要程度--中等
         */
        DEFAULT,
        /**
         * 重要程度--高
         */
        HIGH
    }

    private String channelId;
    private String channelName;
    private ChannelLevel channelLevel;

    private Bitmap icon;
    @DrawableRes
    private int smallIconResId;
    @DrawableRes
    private int largeIconResId;


    private CloudNetWorkNotificationInfo(Builder builder) {

    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Builder() {
        }

    }
}
