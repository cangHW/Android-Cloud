package com.proxy.service.api.impl;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

/**
 * @author : cangHX
 * on 2020/09/24  10:12 PM
 */
public class CloudUiCheckBooleanInfo {

    private final String markId;
    private final boolean isValue;
    private final String message;
    @StringRes
    private final int stringId;

    private CloudUiCheckBooleanInfo(Builder builder) {
        this.markId = builder.markId;
        this.isValue = builder.isValue;
        this.message = builder.message;
        this.stringId = builder.stringId;
    }

    public String getMarkId() {
        return markId;
    }

    public boolean isValue() {
        return isValue;
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
        private boolean isValue = false;
        private String message = "";
        @StringRes
        private int stringId;

        private Builder(String markId) {
            this.markId = markId;
        }

        /**
         * 要求值
         */
        public Builder setValue(boolean value) {
            this.isValue = value;
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

        public CloudUiCheckBooleanInfo build() {
            return new CloudUiCheckBooleanInfo(this);
        }
    }

}
