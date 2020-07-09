package com.proxy.service.ui.annotations;

import androidx.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ViewGroup 的类型
 *
 * @author: cangHX
 * on 2020/07/01  18:50
 */
@IntDef({
        ViewGroupType.ERROR,
        ViewGroupType.LINEAR_LAYOUT,
        ViewGroupType.RELATIVE_LAYOUT,
        ViewGroupType.FRAME_LAYOUT,
        ViewGroupType.VIEW_PAGER,
        ViewGroupType.LINEAR_LAYOUT_COMPAT,
        ViewGroupType.LIST_VIEW,
        ViewGroupType.RECYCLER_VIEW,
        ViewGroupType.SCROLL_VIEW,
        ViewGroupType.HORIZONTAL_SCROLL_VIEW
})
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface ViewGroupType {

    /**
     * 不支持的类型
     */
    int ERROR = -1;

    /**
     * LinearLayout
     */
    int LINEAR_LAYOUT = 0;

    /**
     * RelativeLayout
     */
    int RELATIVE_LAYOUT = 1;

    /**
     * FrameLayout
     */
    int FRAME_LAYOUT = 2;

    /**
     * ViewPager
     */
    int VIEW_PAGER = 3;

    /**
     * LinearLayoutCompat
     */
    int LINEAR_LAYOUT_COMPAT = 4;

    /**
     * ListView
     */
    int LIST_VIEW = 5;

    /**
     * RecyclerView
     */
    int RECYCLER_VIEW = 6;

    /**
     * ScrollView
     */
    int SCROLL_VIEW = 7;

    /**
     * HorizontalScrollView
     */
    int HORIZONTAL_SCROLL_VIEW = 8;
}
