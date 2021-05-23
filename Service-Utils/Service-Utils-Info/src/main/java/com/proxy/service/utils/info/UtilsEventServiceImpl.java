package com.proxy.service.utils.info;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.event.CloudMainThreadEventCallback;
import com.proxy.service.api.event.CloudWorkThreadEventCallback;
import com.proxy.service.api.services.CloudUtilsEventService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.utils.event.DefaultCache;
import com.proxy.service.utils.event.LifecycleCache;

/**
 * @author : cangHX
 * on 2021/05/17  8:28 PM
 */
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_EVENT)
public class UtilsEventServiceImpl implements CloudUtilsEventService {

    /**
     * 绑定事件监听（弱引用）
     *
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 9:22 PM
     */
    @Override
    public void bind(@NonNull CloudMainThreadEventCallback callback) {
        DefaultCache.INSTANCE.addEventCallback(callback.getMainEventTypes(), callback);
    }

    /**
     * 绑定事件监听（弱引用）
     *
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 9:22 PM
     */
    @Override
    public void bind(@NonNull CloudWorkThreadEventCallback callback) {
        DefaultCache.INSTANCE.addEventCallback(callback.getWorkEventTypes(), callback);
    }

    /**
     * 绑定事件监听（生命周期默认与 Activity 相同），建议放在 Activity 的 onStart 或 onCreate 方法中进行绑定
     *
     * @param activity : 上下文，用于提供生命周期，
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 8:57 PM
     */
    @Override
    public void bind(Activity activity, @NonNull CloudMainThreadEventCallback callback) {
        if (activity == null) {
            DefaultCache.INSTANCE.addEventCallback(callback.getMainEventTypes(), callback);
            return;
        }
        LifecycleCache.INSTANCE.addCallback(activity, callback.getMainEventTypes(), callback);
    }

    /**
     * 绑定事件监听（生命周期默认与 Activity 相同），建议放在 Activity 的 onStart 或 onCreate 方法中进行绑定
     *
     * @param activity : 上下文，用于提供生命周期，
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 8:57 PM
     */
    @Override
    public void bind(Activity activity, @NonNull CloudWorkThreadEventCallback callback) {
        if (activity == null) {
            DefaultCache.INSTANCE.addEventCallback(callback.getWorkEventTypes(), callback);
            return;
        }
        LifecycleCache.INSTANCE.addCallback(activity, callback.getWorkEventTypes(), callback);
    }

    /**
     * 绑定事件监听（生命周期默认与 Fragment 相同），建议放在 Fragment 的 onStart 或 onCreate 方法中进行绑定
     *
     * @param fragment : 上下文，用于提供生命周期，
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 8:57 PM
     */
    @Override
    public void bind(Fragment fragment, @NonNull CloudMainThreadEventCallback callback) {
        if (fragment == null) {
            DefaultCache.INSTANCE.addEventCallback(callback.getMainEventTypes(), callback);
            return;
        }
        LifecycleCache.INSTANCE.addCallback(fragment, callback.getMainEventTypes(), callback);
    }

    /**
     * 绑定事件监听（生命周期默认与 Fragment 相同），建议放在 Fragment 的 onStart 或 onCreate 方法中进行绑定
     *
     * @param fragment : 上下文，用于提供生命周期，
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 8:57 PM
     */
    @Override
    public void bind(Fragment fragment, @NonNull CloudWorkThreadEventCallback callback) {
        if (fragment == null) {
            DefaultCache.INSTANCE.addEventCallback(callback.getWorkEventTypes(), callback);
            return;
        }
        LifecycleCache.INSTANCE.addCallback(fragment, callback.getWorkEventTypes(), callback);
    }

    /**
     * 移除事件监听
     *
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/17 8:03 PM
     */
    @Override
    public void remove(@NonNull CloudMainThreadEventCallback callback) {
        DefaultCache.INSTANCE.remove(callback);
        LifecycleCache.INSTANCE.remove(callback);
    }

    /**
     * 移除事件监听
     *
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/17 8:03 PM
     */
    @Override
    public void remove(@NonNull CloudWorkThreadEventCallback callback) {
        DefaultCache.INSTANCE.remove(callback);
        LifecycleCache.INSTANCE.remove(callback);
    }

    /**
     * 同步事件，根据数据类型唤醒符合条件的全部监听
     *
     * @param object : 准备透传的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 9:02 PM
     */
    @Override
    public void set(@NonNull Object object) {
        DefaultCache.INSTANCE.send(object);
        LifecycleCache.INSTANCE.send(object, false);
    }

    /**
     * 异步事件，根据数据类型唤醒符合条件的全部监听，回调时机--UI展示.
     * 注意--如果连续发送多次同一个数据，则在数据被响应前，后一个数据会替换前一个.
     * （如果对应监听未与生命周期绑定，则会被切换为同步事件）
     *
     * @param object : 准备透传的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 9:04 PM
     */
    @Override
    public void postOrSet(@NonNull Object object) {
        DefaultCache.INSTANCE.send(object);
        LifecycleCache.INSTANCE.send(object, true);
    }

    /**
     * 异步事件，根据数据类型唤醒符合条件的全部监听，回调时机--UI展示.
     * 注意--如果连续发送多次同一个数据，则在数据被响应前，后一个数据会替换前一个.
     * （如果对应监听未与生命周期绑定，则会被跳过）
     *
     * @param object : 准备透传的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 9:04 PM
     */
    @Override
    public void postOrSkip(@NonNull Object object) {
        LifecycleCache.INSTANCE.send(object, true);
    }
}
