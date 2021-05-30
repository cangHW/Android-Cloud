package com.proxy.service.utils.event.lifecycle;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.proxy.service.api.context.LifecycleState;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.event.CloudMainThreadEventCallback;
import com.proxy.service.api.event.CloudWorkThreadEventCallback;
import com.proxy.service.api.event.Event;
import com.proxy.service.api.lifecycle.CloudActivityLifecycleListener;
import com.proxy.service.api.lifecycle.CloudFragmentLifecycleListener;
import com.proxy.service.api.lifecycle.FragmentLifecycleState;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.info.UtilsLifecycleServiceImpl;
import com.proxy.service.utils.thread.ThreadManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : cangHX
 * on 2021/05/20  8:19 PM
 */
public class LifecycleHolder implements Lifecycle, CloudActivityLifecycleListener, CloudFragmentLifecycleListener {

    private Event mEvent;
    private final ArrayList<Object> mArrayList = new ArrayList<>();
    private final HashSet<Class<?>> mHashSet = new HashSet<>();

    private final AtomicBoolean isResume = new AtomicBoolean(false);
    private final AtomicBoolean isDestroy = new AtomicBoolean(false);

    public LifecycleHolder(Event event) {
        this.mEvent = event;
    }

    /**
     * 是否已被销毁
     *
     * @return true 已销毁，false 未销毁
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 8:16 PM
     */
    @Override
    public boolean isDestroy() {
        return isDestroy.get();
    }

    /**
     * 销毁
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 8:16 PM
     */
    @Override
    public void destroy() {
        UtilsLifecycleServiceImpl service = new UtilsLifecycleServiceImpl();
        try {
            service.remove((CloudActivityLifecycleListener) this);
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
        try {
            service.remove((CloudFragmentLifecycleListener) this);
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
    }

    /**
     * 添加 Event 类型
     *
     * @param set : Event 类型集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 8:16 PM
     */
    @Override
    public void addClasses(Set<Class<?>> set) {
        if (set == null || set.size() == 0) {
            return;
        }
        mHashSet.addAll(set);
    }

    /**
     * 检查是否处理当前 Event 类型
     *
     * @param aClass : Event 类型
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 8:16 PM
     */
    @Override
    public boolean contains(Class<?> aClass) {
        for (Class<?> aClass1 : new HashSet<>(mHashSet)) {
            if (aClass1.isAssignableFrom(aClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 发送同步消息
     *
     * @param object : 消息体
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 8:16 PM
     */
    @Override
    public void set(final Object object) {
        if (isDestroy()) {
            Logger.Debug(CloudApiError.NO_RUNNING_ACTIVITY.build());
            return;
        }

        if (mEvent instanceof CloudMainThreadEventCallback) {
            ThreadManager.postMain(new Runnable() {
                @Override
                public void run() {
                    if (mEvent == null) {
                        return;
                    }
                    try {
                        ((CloudMainThreadEventCallback) mEvent).onMainEvent(object);
                    } catch (Throwable throwable) {
                        Logger.Debug(throwable);
                    }
                }
            });
        } else if (mEvent instanceof CloudWorkThreadEventCallback) {
            ThreadManager.postWork(new Runnable() {
                @Override
                public void run() {
                    if (mEvent == null) {
                        return;
                    }
                    try {
                        ((CloudWorkThreadEventCallback) mEvent).onWorkEvent(object);
                    } catch (Throwable throwable) {
                        Logger.Debug(throwable);
                    }
                }
            });
        }
    }

    /**
     * 发送异步消息
     *
     * @param object : 消息体
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 8:16 PM
     */
    @Override
    public void post(Object object) {
        if (isDestroy()) {
            Logger.Debug(CloudApiError.NO_RUNNING_ACTIVITY.build());
            return;
        }
        mArrayList.remove(object);
        mArrayList.add(object);
        notifyEvent();
    }

    private synchronized void notifyEvent() {
        if (!isResume.get()) {
            return;
        }
        if (mEvent == null) {
            return;
        }
        for (final Object o : new ArrayList<>(mArrayList)) {
            if (mEvent instanceof CloudMainThreadEventCallback) {
                ThreadManager.postMain(new Runnable() {
                    @Override
                    public void run() {
                        if (!isResume.get()) {
                            return;
                        }
                        try {
                            ((CloudMainThreadEventCallback) mEvent).onMainEvent(o);
                            mArrayList.remove(o);
                        } catch (Throwable throwable) {
                            Logger.Debug(throwable);
                        }
                    }
                });
            } else if (mEvent instanceof CloudWorkThreadEventCallback) {
                ThreadManager.postWork(new Runnable() {
                    @Override
                    public void run() {
                        if (!isResume.get()) {
                            return;
                        }
                        try {
                            ((CloudWorkThreadEventCallback) mEvent).onWorkEvent(o);
                            mArrayList.remove(o);
                        } catch (Throwable throwable) {
                            Logger.Debug(throwable);
                        }
                    }
                });
            }
        }
    }

    /**
     * 回调生命周期
     *
     * @param activity       : 准备观察生命周期的对象
     * @param lifecycleState : 生命周期状态回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/07/15  2:11 PM
     */
    @Override
    public void onLifecycleChanged(Activity activity, LifecycleState lifecycleState) {
        switch (lifecycleState) {
            case LIFECYCLE_PAUSE:
                isResume.set(false);
                break;
            case LIFECYCLE_RESUME:
                isResume.set(true);
                notifyEvent();
                break;
            case LIFECYCLE_DESTROY:
                isResume.set(false);
                isDestroy.set(true);
                mEvent = null;
                break;
        }
    }

    /**
     * 回调生命周期
     *
     * @param fragment       : 准备观察生命周期的对象
     * @param lifecycleState : 生命周期状态回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/05/20  9:56 PM
     */
    @Override
    public void onLifecycleChanged(Fragment fragment, FragmentLifecycleState lifecycleState) {
        switch (lifecycleState) {
            case LIFECYCLE_PAUSE:
            case LIFECYCLE_HIDE:
                isResume.set(false);
                break;
            case LIFECYCLE_RESUME:
            case LIFECYCLE_SHOW:
                isResume.set(true);
                notifyEvent();
                break;
            case LIFECYCLE_DESTROY:
                isResume.set(false);
                isDestroy.set(true);
                mEvent = null;
                break;
        }
    }
}
