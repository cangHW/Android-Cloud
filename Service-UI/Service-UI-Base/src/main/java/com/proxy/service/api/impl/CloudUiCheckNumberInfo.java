package com.proxy.service.api.impl;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

/**
 * @author : cangHX
 * on 2020/09/24  10:21 PM
 */
public class CloudUiCheckNumberInfo {

    private final String markId;
    private final double max;
    private final double maxNotSame;
    private final double min;
    private final double minNotSame;
    private final int scale;
    private final String message;
    @StringRes
    private final int stringId;

    private CloudUiCheckNumberInfo(Builder builder) {
        this.markId = builder.markId;
        this.max = builder.max;
        this.maxNotSame = builder.maxNotSame;
        this.min = builder.min;
        this.minNotSame = builder.minNotSame;
        this.scale = builder.scale;
        this.message = builder.message;
        this.stringId = builder.stringId;
    }

    public String getMarkId() {
        return markId;
    }

    public double getMax() {
        return max;
    }

    public double getMaxNotSame() {
        return maxNotSame;
    }

    public double getMin() {
        return min;
    }

    public double getMinNotSame() {
        return minNotSame;
    }

    public int getScale() {
        return scale;
    }

    public String getMessage() {
        return message;
    }

    public int getStringId() {
        return stringId;
    }

    /**
     * 创建构造体
     *
     * @param markId : 标记id
     */
    public static Builder builder(@NonNull String markId) {
        return new Builder(markId);
    }

    public static class Builder {

        private String markId;
        private double max = Double.MAX_VALUE;
        private double maxNotSame = Double.MAX_VALUE;
        private double min = Double.MIN_VALUE;
        private double minNotSame = Double.MIN_VALUE;
        private int scale = -1;
        private String message = "";
        @StringRes
        private int stringId;

        private Builder(String markId) {
            this.markId = markId;
        }

        /**
         * 最大值，<=
         */
        public Builder setMax(double max) {
            this.max = max;
            return this;
        }

        /**
         * 最大值，<
         */
        public Builder setMaxNotSame(double maxNotSame) {
            this.maxNotSame = maxNotSame;
            return this;
        }

        /**
         * 最小值，>=
         */
        public Builder setMin(double min) {
            this.min = min;
            return this;
        }

        /**
         * 最小值，>
         */
        public Builder setMinNotSame(double minNotSame) {
            this.minNotSame = minNotSame;
            return this;
        }

        /**
         * 小数长度
         */
        public Builder setScale(int scale) {
            this.scale = scale;
            return this;
        }

        /**
         * 校验失败后的错误信息
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 校验失败后的错误信息的资源 id，与 message 取其一，优先 message
         */
        public Builder setStringId(int stringId) {
            this.stringId = stringId;
            return this;
        }

        public CloudUiCheckNumberInfo build() {
            return new CloudUiCheckNumberInfo(this);
        }
    }
}
