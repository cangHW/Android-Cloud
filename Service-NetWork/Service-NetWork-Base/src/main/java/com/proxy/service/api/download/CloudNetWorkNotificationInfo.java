package com.proxy.service.api.download;

import android.graphics.Bitmap;
import android.text.TextUtils;

import androidx.annotation.DrawableRes;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.services.CloudUtilsAppService;
import com.proxy.service.api.services.CloudUtilsBitmapService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;

/**
 * @author : cangHX
 * on 2020/09/02  8:03 PM
 */
public final class CloudNetWorkNotificationInfo {

    public enum ChannelLevel {
        /**
         * 完全不重要，通知允许被折叠
         */
        NONE(0),
        /**
         * 稍微重要，通知允许被折叠
         */
        MIN(1),
        /**
         * 重要程度--低
         */
        LOW(2),
        /**
         * 重要程度--中等
         */
        DEFAULT(3),
        /**
         * 重要程度--高
         */
        HIGH(4);

        private int level;

        ChannelLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }

    private final boolean isError;


    private final String channelId;
    private final String channelName;
    private final ChannelLevel channelLevel;

    private final Bitmap largeIcon;
    @DrawableRes
    private final int smallIconResId;
    @DrawableRes
    private final int largeIconResId;

    private CloudNetWorkNotificationInfo(Builder builder) {
        this.isError = builder.isError;
        this.channelId = builder.channelId;
        this.channelName = builder.channelName;
        this.channelLevel = builder.channelLevel;
        this.largeIcon = builder.largeIcon;
        this.smallIconResId = builder.smallIconResId;
        this.largeIconResId = builder.largeIconResId;
    }

    public boolean isError() {
        return isError;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public ChannelLevel getChannelLevel() {
        return channelLevel;
    }

    public Bitmap getLargeIcon() {
        return largeIcon;
    }

    public int getSmallIconResId() {
        return smallIconResId;
    }

    public int getLargeIconResId() {
        return largeIconResId;
    }

    public Builder build() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private boolean isError = false;

        private String channelId;
        private String channelName;
        private ChannelLevel channelLevel;

        private Bitmap largeIcon;
        @DrawableRes
        private int smallIconResId;
        @DrawableRes
        private int largeIconResId;

        private Builder() {
        }

        private Builder(CloudNetWorkNotificationInfo info) {
            this.isError = info.isError;
            this.channelId = info.channelId;
            this.channelName = info.channelName;
            this.channelLevel = info.channelLevel;
            this.largeIcon = info.largeIcon;
            this.smallIconResId = info.smallIconResId;
            this.largeIconResId = info.largeIconResId;
        }

        public Builder setChannelId(String channelId) {
            this.channelId = channelId;
            return this;
        }

        public Builder setChannelName(String channelName) {
            this.channelName = channelName;
            return this;
        }

        public Builder setChannelLevel(ChannelLevel channelLevel) {
            this.channelLevel = channelLevel;
            return this;
        }

        public Builder setLargeIcon(Bitmap largeIcon) {
            this.largeIcon = largeIcon;
            return this;
        }

        public Builder setSmallIconResId(int smallIconResId) {
            this.smallIconResId = smallIconResId;
            return this;
        }

        public Builder setLargeIconResId(int largeIconResId) {
            this.largeIconResId = largeIconResId;
            return this;
        }

        public CloudNetWorkNotificationInfo build() {
            if (TextUtils.isEmpty(channelId)) {
                isError = true;
                Logger.Debug("The ChannelId can not be null");
            }
            if (TextUtils.isEmpty(channelName)) {
                isError = true;
                Logger.Debug("The ChannelName can not be null");
            }
            if (channelLevel == null) {
                channelLevel = ChannelLevel.DEFAULT;
                Logger.Warning("There is missing ChannelLevel");
            }

            if (smallIconResId == 0) {
                smallIconResId = android.R.drawable.stat_sys_download;
                Logger.Warning("There is missing SmallIconResId");
            }

            if (largeIcon == null && largeIconResId != 0) {
                CloudUtilsBitmapService bitmapService = CloudSystem.getService(CloudServiceTagUtils.UTILS_BITMAP);
                if (bitmapService != null) {
                    largeIcon = bitmapService.toBitmap(largeIconResId);
                }
            }

            if (largeIcon == null) {
                Logger.Warning("There is missing LargeIconResId or LargeIcon");
                CloudUtilsAppService appService = CloudSystem.getService(CloudServiceTagUtils.UTILS_APP);
                if (appService != null) {
                    largeIcon = appService.getIcon();
                }
            }

            return new CloudNetWorkNotificationInfo(this);
        }
    }
}
