package com.proxy.service.api.context;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.context.cache.ActivityStack;

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
        } else {
            application = (Application) context.getApplicationContext();
        }
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbackManager.create());
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
}
