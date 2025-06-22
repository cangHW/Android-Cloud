package com.proxy.service.api.log.base;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: cangHX
 * @data: 2024/11/15 09:58
 * @desc:
 */
public abstract class LogTree implements IL {

    private static final int MAX_TAG_LENGTH = 23;
    private static final int CALL_STACK_INDEX = 5;
    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");

    private final ThreadLocal<String> explicitTag = new ThreadLocal<>();

    public void setTag(@NonNull String tag) {
        explicitTag.set(tag);
    }

    private @Nullable String getTag() {
        String tag = explicitTag.get();
        if (tag != null) {
            explicitTag.remove();
        }
        return tag;
    }

    protected @NonNull String getClassName() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length <= CALL_STACK_INDEX) {
            throw new IllegalStateException("Synthetic stacktrace didn't have enough elements: are you using proguard?");
        }
        return createStackElementTag(stackTrace[CALL_STACK_INDEX]);
    }

    private @NonNull String createStackElementTag(StackTraceElement element) {
        String tag = element.getClassName();
        Matcher m = ANONYMOUS_CLASS.matcher(tag);
        if (m.find()) {
            tag = m.replaceAll("");
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1);
        if (tag.length() <= MAX_TAG_LENGTH) {
            return tag;
        }
        return tag.substring(0, MAX_TAG_LENGTH);
    }

    @Override
    public void v(@NonNull String message, @NonNull Object[] args) {
        prepareLog(Log.VERBOSE, null, message, args);
    }

    @Override
    public void v(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        prepareLog(Log.VERBOSE, throwable, message, args);
    }

    @Override
    public void v(@NonNull Throwable throwable) {
        prepareLog(Log.VERBOSE, throwable, null);
    }

    @Override
    public void d(@NonNull String message, @NonNull Object... args) {
        prepareLog(Log.DEBUG, null, message, args);
    }

    @Override
    public void d(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        prepareLog(Log.DEBUG, throwable, message, args);
    }

    @Override
    public void d(@NonNull Throwable throwable) {
        prepareLog(Log.DEBUG, throwable, null);
    }

    @Override
    public void i(@NonNull String message, @NonNull Object... args) {
        prepareLog(Log.INFO, null, message, args);
    }

    @Override
    public void i(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        prepareLog(Log.INFO, throwable, message, args);
    }

    @Override
    public void i(@NonNull Throwable throwable) {
        prepareLog(Log.INFO, throwable, null);
    }

    @Override
    public void w(@NonNull String message, @NonNull Object... args) {
        prepareLog(Log.WARN, null, message, args);
    }

    @Override
    public void w(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        prepareLog(Log.WARN, throwable, message, args);
    }

    @Override
    public void w(@NonNull Throwable throwable) {
        prepareLog(Log.WARN, throwable, null);
    }

    @Override
    public void e(@NonNull String message, @NonNull Object... args) {
        prepareLog(Log.ERROR, null, message, args);
    }

    @Override
    public void e(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        prepareLog(Log.ERROR, throwable, message, args);
    }

    @Override
    public void e(@NonNull Throwable throwable) {
        prepareLog(Log.ERROR, throwable, null);
    }

    @Override
    public void wtf(@NonNull String message, @NonNull Object... args) {
        prepareLog(Log.ASSERT, null, message, args);
    }

    @Override
    public void wtf(@NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        prepareLog(Log.ASSERT, throwable, message, args);
    }

    @Override
    public void wtf(@NonNull Throwable throwable) {
        prepareLog(Log.ASSERT, throwable, null);
    }

    @Override
    public void log(int priority, @NonNull String message, @NonNull Object... args) {
        prepareLog(priority, null, message, args);
    }

    @Override
    public void log(int priority, @NonNull Throwable throwable, @NonNull String message, @NonNull Object... args) {
        prepareLog(priority, throwable, message, args);
    }

    @Override
    public void log(int priority, @NonNull Throwable throwable) {
        prepareLog(priority, throwable, null);
    }

    private void prepareLog(
            int priority,
            @Nullable Throwable throwable,
            @Nullable String message,
            @NonNull Object... args
    ) {
        var msg = message;
        if (TextUtils.isEmpty(msg)) {
            if (throwable == null) {
                return;
            }
            msg = getStackTraceString(throwable);
        } else {
            if (args.length > 0) {
                msg = formatMessage(msg, args);
            }
            if (throwable != null) {
                msg += "\n " + getStackTraceString(throwable);
            }
        }

        String tag = getTag();
        if (TextUtils.isEmpty(tag)) {
            tag = getClassName();
        }

        onLog(priority, tag, msg, throwable);
    }

    private @NonNull String formatMessage(@NonNull String message, @NonNull Object... args) {
        return String.format(message, args);
    }

    private @NonNull String getStackTraceString(@NonNull Throwable throwable) {
        StringWriter sw = new StringWriter(256);
        PrintWriter pw = new PrintWriter(sw, false);
        throwable.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /**
     * 日志回调
     *
     * @param priority 日志级别, 参考[Log]的级别
     */
    protected abstract void onLog(
            int priority,
            @NonNull String tag,
            @NonNull String message,
            @Nullable Throwable throwable
    );

}