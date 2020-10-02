package com.proxy.service.api.bitmap;

import android.graphics.Color;

import androidx.annotation.ColorInt;

import com.proxy.service.api.utils.Logger;

/**
 * @author : cangHX
 * on 2020/09/23  10:16 PM
 */
public class CloudPrintWordMessage {

    @ColorInt
    private final int textColor;
    private final int textSize;
    private final int strokeWidth;
    private final boolean isBold;
    private final float left;
    private final float top;

    private CloudPrintWordMessage(Builder builder) {
        this.textColor = builder.textColor;
        this.textSize = builder.textSize;
        this.strokeWidth = builder.strokeWidth;
        this.isBold = builder.isBold;
        this.left = builder.left;
        this.top = builder.top;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public boolean isBold() {
        return isBold;
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        @ColorInt
        private int textColor = Color.BLACK;
        private int textSize = 20;
        private int strokeWidth = 4;
        private boolean isBold = false;
        private float left = -1;
        private float top = -1;

        private Builder() {
        }

        /**
         * 字体颜色，默认：黑色
         */
        public Builder setTextColor(@ColorInt int textColor) {
            this.textColor = textColor;
            return this;
        }

        /**
         * 字体尺寸，默认：20
         */
        public Builder setTextSize(int textSize) {
            if (textSize > 0) {
                this.textSize = textSize;
            } else {
                Logger.Warning("The TextSize can not be <= 0");
            }
            return this;
        }

        /**
         * 画笔粗细，默认：4
         */
        public Builder setStrokeWidth(int strokeWidth) {
            if (strokeWidth > 0) {
                this.strokeWidth = strokeWidth;
            } else {
                Logger.Warning("The StrokeWidth can not be <= 0");
            }
            return this;
        }

        /**
         * 是否加粗，默认：false
         */
        public Builder setBold(boolean bold) {
            isBold = bold;
            return this;
        }

        /**
         * 文字 left 与画布 left 位置的距离，默认：居中
         */
        public Builder setLeft(float left) {
            this.left = left;
            return this;
        }

        /**
         * 文字 bottom 与画布 top 位置的距离，默认：居中
         */
        public Builder setTop(float top) {
            this.top = top;
            return this;
        }

        public CloudPrintWordMessage build() {
            return new CloudPrintWordMessage(this);
        }
    }

}
