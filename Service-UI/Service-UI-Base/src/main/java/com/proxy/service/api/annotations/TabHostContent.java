package com.proxy.service.api.annotations;

import androidx.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author: cangHX
 * on 2020/07/02  11:18
 */
@IntDef({

})
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface TabHostContent {

    /**
     * fragment
     * */
    int FRAGMENT = 0;

    /**
     * view
     * */
    int VIEW = 1;
}
