package com.proxy.service.api.multidex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.proxy.service.api.utils.Logger;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * dex资源相关
 *
 * @author: cangHX
 * on 2020/06/22  13:57
 */
class SourceDex {

    private static final String EXTRACTED_NAME_EXT = ".classes";
    private static final String EXTRACTED_SUFFIX = ".zip";

    private static final String SECONDARY_FOLDER_NAME = "code_cache" + File.separator + "secondary-dexes";

    private static final String PREFS_FILE = "multidex.version";
    private static final String KEY_DEX_NUMBER = "dex.number";

    /**
     * 获取dex文件路径
     *
     * @param context : 上下文环境
     * @return dex文件路径集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-22 13:58
     */
    static List<String> getDexPaths(Context context) {
        List<String> dexPaths = new ArrayList<>();

        PackageManager packageManager = null;
        try {
            packageManager = context.getPackageManager();
        } catch (Throwable ignored) {
        }
        if (packageManager == null) {
            return dexPaths;
        }

        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (Throwable ignored) {
        }
        if (applicationInfo == null) {
            return dexPaths;
        }

        dexPaths.add(applicationInfo.sourceDir);

        File sourceApk = new File(applicationInfo.sourceDir);
        String extractedFilePrefix = sourceApk.getName() + EXTRACTED_NAME_EXT;

        if (!VmMultiDex.isVmMultiDexCapable()) {
            int totalDexNumber = getMultiDexPreferences(context).getInt(KEY_DEX_NUMBER, 1);
            File dexDir = new File(applicationInfo.dataDir, SECONDARY_FOLDER_NAME);

            for (int secondaryNumber = 2; secondaryNumber <= totalDexNumber; secondaryNumber++) {
                String fileName = extractedFilePrefix + secondaryNumber + EXTRACTED_SUFFIX;
                File extractedFile = new File(dexDir, fileName);
                if (!extractedFile.isFile()) {
                    Logger.Error("Missing extracted secondary dex file '" + extractedFile.getPath() + "'");
                    dexPaths.clear();
                    return dexPaths;
                }
                dexPaths.add(extractedFile.getAbsolutePath());
            }
        }

        return dexPaths;
    }

    private static SharedPreferences getMultiDexPreferences(Context context) {
        return context.getSharedPreferences(PREFS_FILE, Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ? Context.MODE_PRIVATE : Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
    }
}
