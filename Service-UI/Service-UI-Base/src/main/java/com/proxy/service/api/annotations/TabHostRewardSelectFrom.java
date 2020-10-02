package com.proxy.service.api.annotations;

import androidx.annotation.StringDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author: cangHX
 * on 2020/06/29  16:08
 */
@StringDef({
        TabHostRewardSelectFrom.FROM_CONTENT,
        TabHostRewardSelectFrom.FROM_HELPER,
        TabHostRewardSelectFrom.FROM_TAB
})
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface TabHostRewardSelectFrom {

    /**
     * 因 content 的变化发起选中，例如：viewpager 滑动
     */
    String FROM_CONTENT = "content";

    /**
     * 通过 IRewardHelper 发起选中
     * {@link com.proxy.service.api.interfaces.IRewardHelper}
     */
    String FROM_HELPER = "helper";

    /**
     * 点击 tab 发起选中
     */
    String FROM_TAB = "tab";
}
