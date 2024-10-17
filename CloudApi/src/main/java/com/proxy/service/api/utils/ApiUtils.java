package com.proxy.service.api.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import androidx.annotation.Nullable;

/**
 * @author : cangHX
 * on 2020/08/31  9:28 PM
 */
public class ApiUtils {

    /**
     * 从 context 中获取真实的 activity
     *
     * @param context : 上下文
     * @return 真实的 activity
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/31 9:27 PM
     */
    @Nullable
    public static Activity getActivityFromContext(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

}
