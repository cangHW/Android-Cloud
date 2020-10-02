package com.proxy.androidcloud.helper;

import android.app.Activity;

import androidx.annotation.LayoutRes;

/**
 * @author : cangHX
 * on 2020/09/23  11:06 PM
 */
public abstract class AbstractDetailHelper {

    public abstract @LayoutRes int getLayoutId();

    public abstract void init(Activity activity);
}
