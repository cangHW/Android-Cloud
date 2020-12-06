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

            /**
             * 设置 item ID
             */
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

    /**
     * 设置刷新回调接口
     *
     * @param refreshListener : 刷新回调接口
     */
    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    /**
     * 刷新页面
     */
    protected void refresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    /**
     * 创建 item 信息
     *
     * @return item 信息集合
     */
    public abstract List<HelperItemInfo> createItems();

    /**
     * item 点击
     *
     * @param context  : 上下文
     * @param itemInfo : item 信息
     * @param button   : button位置
     *                 1、{@link HelperItemInfo#BUTTON_TITLE},
     *                 2、{@link HelperItemInfo#BUTTON_CENTER},
     *                 3、{@link HelperItemInfo#BUTTON_LEFT},
     *                 4、{@link HelperItemInfo#BUTTON_RIGHT}
     */
    public abstract void onItemClick(Context context, HelperItemInfo itemInfo, int button);
}
