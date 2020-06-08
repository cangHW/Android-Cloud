package com.cloud.api.utils;

import android.util.Log;

import androidx.annotation.NonNull;

/**
 * @author: cangHX
 * on 2020/06/04  16:22
 * <p>
 * 日志打印
 */
public class Logger {

    enum LogType {
        D, I, E
    }

    private static final String TAG = "CLOUD_SERVICES";
    private String mTag = TAG;

    private Logger() {
    }

    private Logger(String tag) {
        this.mTag = tag;
    }

    public static Logger create(@NonNull String tag) {
        return new Logger(tag);
    }

    public void debug(String msg) {
        println(LogType.D, mTag, msg);
    }

    public void info(String msg) {
        println(LogType.I, mTag, msg);
    }

    public void error(String msg) {
        println(LogType.E, mTag, msg);
    }

    public static void Debug(String msg) {
        println(LogType.D, TAG, msg);
    }

    public static void Info(String msg) {
        println(LogType.I, TAG, msg);
    }

    public static void Error(String msg) {
        println(LogType.E, TAG, msg);
    }

    private static void println(LogType logType, String tag, String msg) {
        switch (logType) {
            case D:
                Log.d(tag, msg);
                break;
            case I:
                Log.i(tag, msg);
                break;
            case E:
                Log.e(tag, msg);
                break;
            default:
        }
    }

}
