package com.proxy.service.api.context.cache;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: cangHX
 * on 2020/06/11  11:22
 * <p>
 * activity 缓存栈
 */
public class ActivityStack {

    /**
     * 显示的页面数量，一般为 1 或 0
     */
    private static final AtomicInteger SHOW_COUNT = new AtomicInteger(0);
    /**
     * Application
     */
    private static Application mApplication;
    /**
     * activity 缓存栈
     */
    private static final Stack<Activity> STACK = new Stack<>();

    /**
     * 添加一个activity缓存
     *
     * @param activity : 页面
     * @return 是否成功，true 成功，false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 11:24
     */
    public static boolean add(@NonNull Activity activity) {
        return STACK.add(activity);
    }

    /**
     * 页面展示
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-12 09:55
     */
    public static void resume() {
        SHOW_COUNT.incrementAndGet();
    }

    /**
     * 移除一个activity缓存
     *
     * @param activity : 页面
     * @return 是否成功，true 成功，false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 11:24
     */
    public static boolean remove(@NonNull Activity activity) {
        return STACK.remove(activity);
    }

    /**
     * 页面隐藏
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-12 09:55
     */
    public static void stop() {
        SHOW_COUNT.decrementAndGet();
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
        if (!STACK.empty()) {
            try {
                return STACK.lastElement();
            }catch (Throwable ignored){}
        }
        return null;
    }

    /**
     * 结束所有的页activity面
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 11:48
     */
    public static void finishAllActivity() {
        while (!STACK.empty()) {
            try {
                Activity activity = STACK.pop();
                if (activity == null) {
                    return;
                }
                activity.finish();
            } catch (Throwable ignored) {
            }
        }
    }

    /**
     * Application 获取
     *
     * @return Application
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-12 09:56
     */
    public static Application getApplication() {
        return mApplication;
    }

    /**
     * Application 缓存
     *
     * @param application : 上下文环境
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-12 09:57
     */
    public static void setApplication(Application application) {
        mApplication = application;
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
        return SHOW_COUNT.intValue();
    }
}
