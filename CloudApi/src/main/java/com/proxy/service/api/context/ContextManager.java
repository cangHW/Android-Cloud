package com.proxy.service.api.context;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.context.cache.ActivityStack;
import com.proxy.service.api.context.listener.CloudLifecycleListener;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.ApiUtils;
import com.proxy.service.api.utils.Logger;

/**
 * context管理类
 *
 * @author: cangHX
 * on 2020/06/11  11:09
 */
public class ContextManager {

    /**
     * context管理类初始化
     *
     * @param context : 上下文环境
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 11:55
     */
    public static void init(@NonNull Context context) {
        Application application;
        if (context instanceof Application) {
            application = (Application) context;
        } else if (context instanceof Activity) {
            application = (Application) context.getApplicationContext();
        } else {
            Activity activity = ApiUtils.getActivityFromContext(context);
            if (activity == null) {
                Logger.Error(CloudApiError.UNKNOWN_ERROR.build());
                return;
            }
            application = activity.getApplication();
        }
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbackManager.INSTANCE);
        ActivityStack.setApplication(application);

        if (context instanceof Activity) {
            ActivityStack.add((Activity) context);
        }

    }

    /**
     * 获取当前activity页面
     *
     * @return 当前activity页面
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 11:45
     */
    @Nullable
    public static Activity getCurrentActivity() {
        return ActivityStack.getCurrentActivity();
    }

    /**
     * 结束所有的页activity面
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 11:48
     */
    public static void finishAllActivity() {
        ActivityStack.finishAllActivity();
    }

    /**
     * Application 获取
     *
     * @return Application
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-12 09:56
     */
    @Nullable
    public static Application getApplication() {
        return ActivityStack.getApplication();
    }

    /**
     * 获取展示页面的数量
     *
     * @return 页面的数量
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-12 09:59
     */
    public static int getShowCount() {
        return ActivityStack.getShowCount();
    }

    /**
     * 注册生命周期观察者
     *
     * @param activity          : 准备申请注册的 activity
     * @param lifecycleListener : 生命周期回调对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/15 2:12 PM
     */
    public static void addLifecycleListener(Activity activity, CloudLifecycleListener lifecycleListener, LifecycleState... states) {
        ActivityLifecycleCallbackManager.addLifecycleListener(activity, lifecycleListener, states);
    }
}
