package com.proxy.service.api.context;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.context.cache.ActivityStack;

/**
 * activity生命周期管理
 *
 * @author: cangHX
 * on 2020/06/11  11:14
 */
class ActivityLifecycleCallbackManager implements Application.ActivityLifecycleCallbacks {

    static ActivityLifecycleCallbackManager create() {
        return new ActivityLifecycleCallbackManager();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        ActivityStack.add(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        ActivityStack.resume();
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        ActivityStack.stop();
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        ActivityStack.remove(activity);
    }
}
