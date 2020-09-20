package com.proxy.service.api.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.error.CloudApiError;

import java.io.File;

/**
 * @author : cangHX
 * on 2020/09/06  11:41 AM
 */
public class PathUtils {

    public static final String CACHE_PREFIX = "mark_";
    public static final String CACHE_SUFFIX = ".temp";

    public static String getDiskCacheDir() {
        String cachePath = "";

        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return cachePath;
        }

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                cachePath = externalCacheDir.getPath() + File.separator;
            }
        }
        if (TextUtils.isEmpty(cachePath)) {
            cachePath = context.getCacheDir().getPath() + File.separator;
        }
        return cachePath;
    }

    public static String getDiskFileDir() {
        String filePath = "";

        Context context = ContextManager.getApplication();
        if (context == null) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return filePath;
        }

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            File externalFileDir = context.getExternalFilesDir("");
            if (externalFileDir != null) {
                filePath = externalFileDir.getPath() + File.separator;
            }
        }
        if (TextUtils.isEmpty(filePath)) {
            filePath = context.getFilesDir().getPath() + File.separator;
        }
        return filePath;
    }

    public static String getNameFromPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        try {
            return path.substring(path.lastIndexOf(File.separator) + 1);
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        return "";
    }

    public static String getDirFromPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        try {
            return path.substring(0, path.lastIndexOf(File.separator) + 1);
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        return "";
    }
}
