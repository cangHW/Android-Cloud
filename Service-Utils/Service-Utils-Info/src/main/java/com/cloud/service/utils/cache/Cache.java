package com.cloud.service.utils.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;

import com.cloud.api.utils.Logger;

import java.lang.ref.SoftReference;

/**
 * @author: cangHX
 * on 2020/06/11  15:27
 * <p>
 * 缓存类，防止重复创建，降低资源消耗
 */
public class Cache {

    private static SoftReference<PackageManager> mPackageManagerSoftReference;
    private static SoftReference<ActivityManager> mActivityManagerSoftReference;

    @Nullable
    public synchronized static PackageManager getPackageManager(Context context) {
        if (mPackageManagerSoftReference != null) {
            PackageManager packageManager = mPackageManagerSoftReference.get();
            if (packageManager != null) {
                return packageManager;
            }
        }
        PackageManager packageManager;
        try {
            packageManager = context.getPackageManager();
        } catch (Throwable throwable) {
            Logger.Debug(throwable.getMessage());
            return null;
        }
        mPackageManagerSoftReference = new SoftReference<>(packageManager);
        return packageManager;
    }

    @Nullable
    public synchronized static ActivityManager getActivityManager(Context context) {
        if (mActivityManagerSoftReference != null) {
            ActivityManager activityManager = mActivityManagerSoftReference.get();
            if (activityManager != null) {
                return activityManager;
            }
        }
        ActivityManager activityManager;
        try {
            activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        } catch (Throwable throwable) {
            Logger.Debug(throwable.getMessage());
            return null;
        }
        mActivityManagerSoftReference = new SoftReference<>(activityManager);
        return activityManager;
    }
}
