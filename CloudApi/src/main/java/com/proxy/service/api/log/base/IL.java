package com.proxy.service.api.log.base;

import androidx.annotation.NonNull;

/**
 * @author: cangHX
 * @data: 2024/4/28 20:20
 * @desc:
 */
public interface IL {

    void v(@NonNull String message, @NonNull Object... args);

    void v(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args);

    void v(@NonNull Throwable throwable);

    void d(@NonNull String message, @NonNull Object... args);

    void d(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args);

    void d(@NonNull Throwable throwable);

    void i(@NonNull String message, @NonNull Object... args);

    void i(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args);

    void i(@NonNull Throwable throwable);

    void w(@NonNull String message, @NonNull Object... args);

    void w(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args);

    void w(@NonNull Throwable throwable);

    void e(@NonNull String message, @NonNull Object... args);

    void e(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args);

    void e(@NonNull Throwable throwable);

    void wtf(@NonNull String message, @NonNull Object... args);

    void wtf(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args);

    void wtf(@NonNull Throwable throwable);

    void log(int priority, @NonNull String message, @NonNull Object... args);

    void log(int priority, @NonNull Throwable throwable, @NonNull String message, @NonNull Object... args);

    void log(int priority, @NonNull Throwable throwable);
}