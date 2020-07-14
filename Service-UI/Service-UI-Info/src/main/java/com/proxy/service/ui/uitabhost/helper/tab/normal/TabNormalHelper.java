package com.proxy.service.ui.uitabhost.helper.tab.normal;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.proxy.service.api.annotations.TabHostRewardSelectFrom;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.ui.annotations.ViewGroupType;
import com.proxy.service.ui.uitabhost.helper.tab.base.AbstractTabHelper;
import com.proxy.service.ui.util.ViewUtils;

/**
 * @author: cangHX
 * on 2020/07/02  14:22
 */
public class TabNormalHelper extends AbstractTabHelper {

    /**
     * 设置容器
     *
     * @param viewGroup : ViewGroup，容器
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-02 11:14
     */
    @SuppressLint("SwitchIntDef")
    @Override
    public void setViewGroup(ViewGroup viewGroup) {
        if (this.mContext == null) {
            Logger.Error("The context is error");
            return;
        }

        int type = ViewUtils.getViewGroupType(viewGroup);

        for (View view : mList) {

            View cover = new View(mContext);
            cover.setOnClickListener(v -> {
                try {
                    int index = mList.indexOf(view);
                    if (!this.mCallback.isCanSelect(index)) {
                        return;
                    }
                    changSelect(mSelect, index, TabHostRewardSelectFrom.FROM_TAB, true);
                } catch (Throwable ignored) {
                }
            });

            FrameLayout layout = new FrameLayout(mContext);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (view != null) {
                layout.addView(view, layoutParams);
            }
            layout.addView(cover, layoutParams);

            int with;
            int height;

            switch (type) {
                case ViewGroupType.LINEAR_LAYOUT:
                    LinearLayout linearLayout = (LinearLayout) viewGroup;
                    int layoutOrientation = linearLayout.getOrientation();
                    with = layoutOrientation == LinearLayout.VERTICAL ? ViewGroup.LayoutParams.MATCH_PARENT : 0;
                    height = layoutOrientation == LinearLayout.HORIZONTAL ? ViewGroup.LayoutParams.MATCH_PARENT : 0;
                    break;
                case ViewGroupType.LINEAR_LAYOUT_COMPAT:
                    LinearLayoutCompat linearLayoutCompat = (LinearLayoutCompat) viewGroup;
                    int compatOrientation = linearLayoutCompat.getOrientation();
                    with = compatOrientation == LinearLayoutCompat.VERTICAL ? ViewGroup.LayoutParams.MATCH_PARENT : 0;
                    height = compatOrientation == LinearLayoutCompat.HORIZONTAL ? ViewGroup.LayoutParams.MATCH_PARENT : 0;
                    break;
                default:
                    with = ViewGroup.LayoutParams.MATCH_PARENT;
                    height = ViewGroup.LayoutParams.MATCH_PARENT;
                    break;
            }

            ViewGroup.LayoutParams viewParams = ViewUtils.getLayoutParams(viewGroup, with, height);

            if (viewParams == null) {
                continue;
            }

            switch (type) {
                case ViewGroupType.LINEAR_LAYOUT:
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewParams;
                    params.weight = 1;
                    break;
                case ViewGroupType.LINEAR_LAYOUT_COMPAT:
                    LinearLayoutCompat.LayoutParams params2 = (LinearLayoutCompat.LayoutParams) viewParams;
                    params2.weight = 1;
                    break;
                default:
                    break;
            }

            viewGroup.addView(layout, viewParams);
        }
    }

    /**
     * 切换选中
     *
     * @param old        : 当前选中
     * @param now        : 即将选中
     * @param from       : 事件来源，{@link TabHostRewardSelectFrom}
     * @param isCallback : 是否进行回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-03 09:59
     */
    @Override
    protected synchronized void changSelect(int old, int now, @TabHostRewardSelectFrom String from, boolean isCallback) {
        if (isCallback) {
            if (old >= 0 && old < mList.size() && old != now) {
                this.mCallback.unSelect(old, from);
            }
            this.mCallback.select(now, from);
        }

        this.mSelect = now;
    }

}
