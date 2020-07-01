package com.proxy.service.ui.annotations;

import androidx.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: cangHX
 * on 2020/07/01  18:50
 */
@IntDef({

})
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ViewGroupType {

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
     * ViewGroup
     */
    int VIEW_GROUP = 3;
}
