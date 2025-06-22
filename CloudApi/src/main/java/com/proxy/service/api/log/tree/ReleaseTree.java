package com.proxy.service.api.log.tree;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author: cangHX
 * @data: 2024/11/15 11:01
 * @desc:
 */
public class ReleaseTree extends DebugTree {

    @Override
    protected void onLog(int priority, @NonNull String tag, @NonNull String message, @Nullable Throwable throwable) {
        if (priority == Log.DEBUG) {
            return;
        }
        super.onLog(priority, tag, message, throwable);
    }
}