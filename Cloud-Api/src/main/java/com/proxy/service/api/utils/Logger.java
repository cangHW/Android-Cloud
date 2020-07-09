package com.proxy.service.api.utils;

import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;

/**
 * 日志打印
 *
 * @author: cangHX
 * on 2020/06/04  16:22
 */
public class Logger {

    enum LogType {
        D, I, W, E
    }

    private static final String TAG = "CLOUD_SERVICES";
    private static boolean isDebug = true;
    private String mTag = TAG;

    private Logger() {
    }

    private Logger(String tag) {
        this.mTag = tag;
    }

    public static Logger create(@NonNull String tag) {
        return new Logger(tag);
    }

    public static void setDebug(boolean debug){
        isDebug = debug;
    }


    public void debug(String msg) {
        debug(msg, null);
    }

    public void debug(Throwable throwable) {
        debug("", throwable);
    }

    public void debug(String msg, Throwable throwable) {
        println(LogType.D, mTag, msg, throwable);
    }


    public void info(String msg) {
        info(msg, null);
    }

    public void info(Throwable throwable) {
        info("", throwable);
    }

    public void info(String msg, Throwable throwable) {
        println(LogType.I, mTag, msg, throwable);
    }


    public void warn(String msg) {
        warn(msg, null);
    }

    public void warn(Throwable throwable) {
        warn("", throwable);
    }

    public void warn(String msg, Throwable throwable) {
        println(LogType.W, mTag, msg, throwable);
    }


    public void error(String msg) {
        error(msg, null);
    }

    public void error(Throwable throwable) {
        error("", throwable);
    }

    public void error(String msg, Throwable throwable) {
        println(LogType.E, mTag, msg, throwable);
    }


    public static void Debug(String msg) {
        Debug(msg, null);
    }

    public static void Debug(Throwable throwable) {
        Debug("", throwable);
    }

    public static void Debug(String msg, Throwable throwable) {
        println(LogType.D, TAG, msg, throwable);
    }


    public static void Info(String msg) {
        Info(msg, null);
    }

    public static void Info(Throwable throwable) {
        Info("", throwable);
    }

    public static void Info(String msg, Throwable throwable) {
        println(LogType.I, TAG, msg, throwable);
    }


    public static void Warning(String msg) {
        Warning(msg, null);
    }

    public static void Warning(Throwable throwable) {
        Warning("", throwable);
    }

    public static void Warning(String msg, Throwable throwable) {
        println(LogType.W, TAG, msg, throwable);
    }


    public static void Error(String msg) {
        Error(msg, null);
    }

    public static void Error(Throwable throwable) {
        Error("", throwable);
    }

    public static void Error(String msg, Throwable throwable) {
        println(LogType.E, TAG, msg, throwable);
    }


    private static synchronized void println(LogType logType, String tag, String msg, Throwable throwable) {
        if (!isDebug){
            return;
        }
        StringBuilder builder = new StringBuilder();

        builder.delete(0, builder.length());
        builder.append("***** ");
        builder.append("START ");
        builder.append("[ ");
        builder.append("Process");
        builder.append(" : ");
        builder.append(Process.myPid());
        builder.append(" ]-[ ");
        builder.append(Thread.currentThread().getName());
        builder.append(" : ");
        builder.append(Thread.currentThread().getId());
        builder.append(" ]");
        builder.append(" *****");
        String startLog = builder.toString();

        builder.delete(0, builder.length());
        builder.append("***** ");
        builder.append("END");
        builder.append(" *****");
        String endLog = builder.toString();

        switch (logType) {
            case D:
                Log.d(tag, startLog);
                if (throwable == null) {
                    Log.d(tag, msg);
                } else {
                    Log.d(tag, msg, throwable);
                }
                Log.d(tag, endLog);
                break;
            case I:
                if (throwable == null) {
                    Log.i(tag, msg);
                } else {
                    Log.i(tag, msg, throwable);
                }
                break;
            case W:
                Log.w(tag, startLog);
                if (throwable == null) {
                    Log.w(tag, msg);
                } else {
                    Log.w(tag, msg, throwable);
                }
                break;
            case E:
                Log.e(tag, startLog);
                if (throwable == null) {
                    Log.e(tag, msg);
                } else {
                    Log.e(tag, msg, throwable);
                }
                Log.e(tag, endLog);
                break;
            default:
        }
    }

}
