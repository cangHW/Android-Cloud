package com.proxy.service.api.impl;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

/**
 * @author : cangHX
 * on 2020/09/24  10:27 PM
 */
public class CloudUiCheckStringInfo {

    private final String markId;
    private final int maxLength;
    private final int maxLengthNotSame;
    private final int minLength;
    private final int minLengthNotSame;
    private final boolean notEmpty;
    private final boolean notBlank;
    private final String notWithRegex;
    private final String shouldWithRegex;
    private final String message;
    @StringRes
    private final int stringId;

    private CloudUiCheckStringInfo(Builder builder) {
        this.markId = builder.markId;
        this.maxLength = builder.maxLength;
        this.maxLengthNotSame = builder.maxLengthNotSame;
        this.minLength = builder.minLength;
        this.minLengthNotSame = builder.minLengthNotSame;
        this.notEmpty = builder.notEmpty;
        this.notBlank = builder.notBlank;
        this.notWithRegex = builder.notWithRegex;
        this.shouldWithRegex = builder.shouldWithRegex;
        this.message = builder.message;
        this.stringId = builder.stringId;
    }

    public String getMarkId() {
        return markId;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public int getMaxLengthNotSame() {
        return maxLengthNotSame;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMinLengthNotSame() {
        return minLengthNotSame;
    }

    public boolean isNotEmpty() {
        return notEmpty;
    }

    public boolean isNotBlank() {
        return notBlank;
    }

    public String getNotWithRegex() {
        return notWithRegex;
    }

    public String getShouldWithRegex() {
        return shouldWithRegex;
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
        private int maxLength = -1;
        private int maxLengthNotSame = -1;
        private int minLength = -1;
        private int minLengthNotSame = -1;
        private boolean notEmpty = false;
        private boolean notBlank = false;
        private String notWithRegex = "";
        private String shouldWithRegex = "";
        private String message = "";
        @StringRes
        private int stringId;

        private Builder(String markId) {
            this.markId = markId;
        }

        /**
         * 最大长度，<=
         */
        public Builder setMaxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        /**
         * 最大长度，<
         */
        public Builder setMaxLengthNotSame(int maxLengthNotSame) {
            this.maxLengthNotSame = maxLengthNotSame;
            return this;
        }

        /**
         * 最小值，>=
         */
        public Builder setMinLength(int minLength) {
            this.minLength = minLength;
            return this;
        }

        /**
         * 最小值，>
         */
        public Builder setMinLengthNotSame(int minLengthNotSame) {
            this.minLengthNotSame = minLengthNotSame;
            return this;
        }

        /**
         * 不能为 null 或 空
         */
        public Builder setNotEmpty(boolean notEmpty) {
            this.notEmpty = notEmpty;
            return this;
        }

        /**
         * 不能为空格
         */
        public Builder setNotBlank(boolean notBlank) {
            this.notBlank = notBlank;
            return this;
        }

        /**
         * 不允许含有的格式，正则表达式
         */
        public Builder setNotWithRegex(String notWithRegex) {
            this.notWithRegex = notWithRegex;
            return this;
        }

        /**
         * 必须含有的格式，正则表达式
         */
        public Builder setShouldWithRegex(String shouldWithRegex) {
            this.shouldWithRegex = shouldWithRegex;
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

        public CloudUiCheckStringInfo build() {
            return new CloudUiCheckStringInfo(this);
        }
    }
}
