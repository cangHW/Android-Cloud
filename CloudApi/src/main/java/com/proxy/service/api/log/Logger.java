package com.proxy.service.api.log;

import androidx.annotation.NonNull;

import com.proxy.service.api.log.tree.TreeGroup;

/**
 * 日志工具
 *
 * @author: cangHX
 * @data: 2024/4/28 17:26
 * @desc:
 */
public class Logger {

    private static final String TAG = "CLOUD_SERVICES";

    private static final TreeGroup tree = new TreeGroup();

    public static void v(@NonNull Throwable throwable) {
        tree.setTag(TAG);
        tree.v(throwable);
    }

    public static void v(@NonNull String message, @NonNull Object... args) {
        tree.setTag(TAG);
        tree.v(message, args);
    }

    public static void v(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        tree.setTag(TAG);
        tree.v(throwable, message, args);
    }

    public static void d(@NonNull Throwable throwable) {
        tree.setTag(TAG);
        tree.d(throwable);
    }

    public static void d(@NonNull String message, @NonNull Object... args) {
        tree.setTag(TAG);
        tree.d(message, args);
    }

    public static void d(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        tree.setTag(TAG);
        tree.d(throwable, message, args);
    }

    public static void i(@NonNull Throwable throwable) {
        tree.setTag(TAG);
        tree.i(throwable);
    }

    public static void i(@NonNull String message, @NonNull Object... args) {
        tree.setTag(TAG);
        tree.i(message, args);
    }

    public static void i(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        tree.setTag(TAG);
        tree.i(throwable, message, args);
    }

    public static void w(@NonNull Throwable throwable) {
        tree.setTag(TAG);
        tree.w(throwable);
    }

    public static void w(@NonNull String message, @NonNull Object... args) {
        tree.setTag(TAG);
        tree.w(message, args);
    }

    public static void w(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        tree.setTag(TAG);
        tree.w(throwable, message, args);
    }

    public static void e(@NonNull Throwable throwable) {
        tree.setTag(TAG);
        tree.e(throwable);
    }

    public static void e(@NonNull String message, @NonNull Object... args) {
        tree.setTag(TAG);
        tree.e(message, args);
    }

    public static void e(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        tree.setTag(TAG);
        tree.e(throwable, message, args);
    }

    public static void wtf(@NonNull Throwable throwable) {
        tree.setTag(TAG);
        tree.wtf(throwable);
    }

    public static void wtf(@NonNull String message, @NonNull Object... args) {
        tree.setTag(TAG);
        tree.wtf(message, args);
    }

    public static void wtf(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        tree.setTag(TAG);
        tree.wtf(throwable, message, args);
    }

    public static void log(int priority, @NonNull String message, @NonNull Object... args) {
        tree.setTag(TAG);
        tree.log(priority, message, args);
    }

    public static void log(int priority, @NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        tree.setTag(TAG);
        tree.log(priority, throwable, message, args);
    }

    public static void log(int priority, @NonNull Throwable throwable) {
        tree.setTag(TAG);
        tree.log(priority, throwable);
    }
}