package com.proxy.service.api.context;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.context.cache.ActivityStack;
import com.proxy.service.api.context.lifecycle.LifecycleBean;
import com.proxy.service.api.context.listener.CloudLifecycleListener;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.api.utils.WeakReferenceUtils;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.WeakHashMap;

/**
 * activity生命周期管理
 *
 * @author: cangHX
 * on 2020/06/11  11:14
 */
enum ActivityLifecycleCallbackManager implements Application.ActivityLifecycleCallbacks {

    /**
     * 单例
     */
    INSTANCE;

    private static final WeakHashMap<Activity, LifecycleBean> ACTIVITY_MAPPER = new WeakHashMap<>();

    /**
     * 注册生命周期回调，弱引用，所以调用方需要保证对象没有回收，防止接收不到回调
     *
     * @param activity          : 准备观察的 activity
     * @param lifecycleListener : 生命周期回调对象
     * @param states            : 准备观察的生命周期状态
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/15 5:11 PM
     */
    static void addLifecycleListener(Activity activity, CloudLifecycleListener lifecycleListener, LifecycleState... states) {
        if (activity == null) {
            Logger.Error(CloudApiError.DATA_EMPTY.setAbout("The activity cannot be empty, in the method addLifecycleListener").build());
            return;
        }
        if (states == null || states.length == 0) {
            return;
        }
        LifecycleBean lifecycle = ACTIVITY_MAPPER.get(activity);
        if (lifecycle == null) {
            lifecycle = new LifecycleBean();
            ACTIVITY_MAPPER.put(activity, lifecycle);
        }
        for (LifecycleState state : states) {
            if (state == null) {
                continue;
            }
            lifecycle.setLifecycleListener(state, lifecycleListener);
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        ActivityStack.add(activity);
        notify(activity, LifecycleState.LIFECYCLE_CREATE);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        notify(activity, LifecycleState.LIFECYCLE_START);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        ActivityStack.resume();
        notify(activity, LifecycleState.LIFECYCLE_RESUME);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        notify(activity, LifecycleState.LIFECYCLE_PAUSE);
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        ActivityStack.stop();
        notify(activity, LifecycleState.LIFECYCLE_STOP);
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        ActivityStack.remove(activity);
        notify(activity, LifecycleState.LIFECYCLE_DESTROY);
        ACTIVITY_MAPPER.remove(activity);
    }

    private static void notify(final Activity activity, final LifecycleState state) {
        LifecycleBean lifecycleBean = ACTIVITY_MAPPER.get(activity);
        if (lifecycleBean == null) {
            return;
        }
        final List<WeakReference<CloudLifecycleListener>> list = lifecycleBean.getLifecycleListener(state);
        WeakReferenceUtils.checkValueIsEmpty(list, new WeakReferenceUtils.Callback<CloudLifecycleListener>() {
            @Override
            public void onCallback(WeakReference<CloudLifecycleListener> weakReference, CloudLifecycleListener lifecycleChangedCallback) {
                lifecycleChangedCallback.onLifecycleChanged(activity, state);
            }
        });
    }
}
