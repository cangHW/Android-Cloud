package com.proxy.androidcloud.helper;

import android.content.Context;

import java.util.List;

/**
 * @author : cangHX
 * on 2020/08/13  10:07 PM
 */
public abstract class AbstractListHelper {

    public interface RefreshListener {
        void onRefresh();
    }

    private RefreshListener refreshListener;

    public static class HelperItemInfo {

        public static final int BUTTON_TITLE = 0;
        public static final int BUTTON_LEFT = 1;
        public static final int BUTTON_CENTER = 2;
        public static final int BUTTON_RIGHT = 3;

        public final int id;
        public final String title;

        public final String leftButton;
        public final String centerButton;
        public final String rightButton;

        public HelperItemInfo(Builder builder) {
            this.id = builder.id;
            this.title = builder.title;

            this.leftButton = builder.leftButton;
            this.centerButton = builder.centerButton;
            this.rightButton = builder.rightButton;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private int id;
            public String title;

            private String leftButton;
            private String centerButton;
            private String rightButton;

            public Builder setId(int id) {
                this.id = id;
                return this;
            }

            public Builder setTitle(String title) {
                this.title = title;
                return this;
            }

            public Builder setLeftButton(String leftButton) {
                this.leftButton = leftButton;
                return this;
            }

            public Builder setCenterButton(String centerButton) {
                this.centerButton = centerButton;
                return this;
            }

            public Builder setRightButton(String rightButton) {
                this.rightButton = rightButton;
                return this;
            }

            public HelperItemInfo build() {
                return new HelperItemInfo(this);
            }
        }
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    protected void refresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    public abstract List<HelperItemInfo> createItems();

    public abstract void onItemClick(Context context, HelperItemInfo itemInfo, int button);
}
