package com.proxy.service.api.context;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.context.cache.ActivityStack;
import com.proxy.service.api.context.listener.CloudLifecycleListener;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * activity生命周期管理
 *
 * @author: cangHX
 * on 2020/06/11  11:14
 */
class ActivityLifecycleCallbackManager implements Application.ActivityLifecycleCallbacks {

    private interface CheckListener {
        /**
         * 检测是否存在目标 listener
         *
         * @param activity : 申请检测的参数
         * @param listener : 检测后的 listener
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/7/15 5:05 PM
         */
        void hasListener(Activity activity, CloudLifecycleListener listener);
    }

    private static final HashMap<String, WeakReference<CloudLifecycleListener>> LIFECYCLE_MAPPER = new HashMap<>();

    static ActivityLifecycleCallbackManager create() {
        return new ActivityLifecycleCallbackManager();
    }

    /**
     * 注册生命周期观察者
     *
     * @param canonicalName     : 准备观察的 activity 的全路径名称，activity.getClass().getCanonicalName()
     * @param lifecycleListener : 生命周期回调对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/15 5:11 PM
     */
    static void addLifecycleListener(String canonicalName, CloudLifecycleListener lifecycleListener) {
        LIFECYCLE_MAPPER.put(canonicalName, new WeakReference<>(lifecycleListener));
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        ActivityStack.add(activity);
        doCheck(activity, new CheckListener() {
            @Override
            public void hasListener(Activity activity1, CloudLifecycleListener listener) {
                listener.onActivityCreated(activity1);
            }
        });
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        doCheck(activity, new CheckListener() {
            @Override
            public void hasListener(Activity activity1, CloudLifecycleListener listener) {
                listener.onActivityStarted(activity1);
            }
        });
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        ActivityStack.resume();
        doCheck(activity, new CheckListener() {
            @Override
            public void hasListener(Activity activity1, CloudLifecycleListener listener) {
                listener.onActivityResumed(activity1);
            }
        });
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        doCheck(activity, new CheckListener() {
            @Override
            public void hasListener(Activity activity1, CloudLifecycleListener listener) {
                listener.onActivityPaused(activity1);
            }
        });
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        ActivityStack.stop();
        doCheck(activity, new CheckListener() {
            @Override
            public void hasListener(Activity activity1, CloudLifecycleListener listener) {
                listener.onActivityStopped(activity1);
            }
        });
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        doCheck(activity, new CheckListener() {
            @Override
            public void hasListener(Activity activity1, CloudLifecycleListener listener) {
                listener.onActivitySaveInstanceState(activity1);
            }
        });
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        ActivityStack.remove(activity);
        doCheck(activity, new CheckListener() {
            @Override
            public void hasListener(Activity activity1, CloudLifecycleListener listener) {
                listener.onActivityDestroyed(activity1);
            }
        });
        doDelete(activity);
    }

    private void doDelete(Activity activity) {
        String canonicalName = activity.getClass().getCanonicalName();
        if (!LIFECYCLE_MAPPER.containsKey(canonicalName)) {
            return;
        }
        LIFECYCLE_MAPPER.remove(canonicalName);
    }

    private void doCheck(Activity activity, CheckListener checkListener) {
        String canonicalName = activity.getClass().getCanonicalName();
        if (!LIFECYCLE_MAPPER.containsKey(canonicalName)) {
            return;
        }
        WeakReference<CloudLifecycleListener> weakReference = LIFECYCLE_MAPPER.get(canonicalName);
        if (weakReference == null) {
            LIFECYCLE_MAPPER.remove(canonicalName);
            return;
        }
        CloudLifecycleListener listener = weakReference.get();
        if (listener == null) {
            LIFECYCLE_MAPPER.remove(canonicalName);
            return;
        }
        checkListener.hasListener(activity, listener);
    }
}
