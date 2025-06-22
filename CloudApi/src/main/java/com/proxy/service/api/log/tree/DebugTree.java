package com.proxy.service.api.log.tree;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.log.base.LogTree;

/**
 * @author: cangHX
 * @data: 2024/11/15 11:01
 * @desc:
 */
public class DebugTree extends LogTree {

    private static final int MAX_LOG_LENGTH = 3000;

    private static boolean printEnable = true;

    public static void setPrintEnable(boolean enable) {
        printEnable = enable;
    }

    @Override
    protected void onLog(int priority, @NonNull String tag, @NonNull String message, @Nullable Throwable throwable) {
        if (!printEnable) {
            return;
        }

        if (message.length() < MAX_LOG_LENGTH) {
            if (priority == Log.ASSERT) {
                Log.wtf(tag, message);
            } else {
                Log.println(priority, tag, message);
            }
            return;
        }

        var i = 0;

        while (i >= 0 && i < message.length()) {
            int newline = message.indexOf('\n', i);
            if (newline == -1) {
                newline = message.length();
            }

            do {
                int end = Math.min(newline, i + MAX_LOG_LENGTH);
                String part = message.substring(i, end);
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, part);
                } else {
                    Log.println(priority, tag, part);
                }
                i = end;
            } while (i < newline);
            i++;
        }
    }
}