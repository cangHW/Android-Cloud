package com.proxy.service.ui.util;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.proxy.service.ui.annotations.ViewGroupType;

/**
 * @author: cangHX
 * on 2020/07/01  18:48
 */
public class ViewUtils {

    @ViewGroupType
    public static int getViewGroupType(ViewGroup viewGroup) {
        if (viewGroup instanceof ScrollView) {
            return ViewGroupType.SCROLL_VIEW;
        } else if (viewGroup instanceof HorizontalScrollView) {
            return ViewGroupType.HORIZONTAL_SCROLL_VIEW;
        } else if (viewGroup instanceof LinearLayout) {
            return ViewGroupType.LINEAR_LAYOUT;
        } else if (viewGroup instanceof RelativeLayout) {
            return ViewGroupType.RELATIVE_LAYOUT;
        } else if (viewGroup instanceof FrameLayout) {
            return ViewGroupType.FRAME_LAYOUT;
        } else if (viewGroup instanceof LinearLayoutCompat) {
            return ViewGroupType.LINEAR_LAYOUT_COMPAT;
        } else if (viewGroup instanceof ViewPager) {
            return ViewGroupType.VIEW_PAGER;
        } else if (viewGroup instanceof ListView) {
            return ViewGroupType.LIST_VIEW;
        }

        try {
            if (viewGroup instanceof RecyclerView) {
                return ViewGroupType.RECYCLER_VIEW;
            }
        } catch (Throwable ignored) {
        }

        return ViewGroupType.ERROR;
    }

    @Nullable
    public static ViewGroup.LayoutParams getLayoutParams(ViewGroup viewGroup, int width, int height) {
        ViewGroup.LayoutParams layoutParams = null;
        int type = getViewGroupType(viewGroup);
        switch (type) {
            case ViewGroupType.SCROLL_VIEW:
            case ViewGroupType.HORIZONTAL_SCROLL_VIEW:
            case ViewGroupType.FRAME_LAYOUT:
                layoutParams = new FrameLayout.LayoutParams(width, height);
                break;
            case ViewGroupType.LINEAR_LAYOUT:
                layoutParams = new LinearLayout.LayoutParams(width, height);
                break;
            case ViewGroupType.RELATIVE_LAYOUT:
                layoutParams = new RelativeLayout.LayoutParams(width, height);
                break;
            case ViewGroupType.LINEAR_LAYOUT_COMPAT:
                layoutParams = new LinearLayoutCompat.LayoutParams(width, height);
                break;
            case ViewGroupType.VIEW_PAGER:
            case ViewGroupType.LIST_VIEW:
            case ViewGroupType.RECYCLER_VIEW:
            case ViewGroupType.ERROR:
            default:
                break;
        }
        return layoutParams;
    }
}
