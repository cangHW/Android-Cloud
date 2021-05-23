package com.proxy.service.api.services;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.proxy.service.api.event.CloudMainThreadEventCallback;
import com.proxy.service.api.event.CloudWorkThreadEventCallback;
import com.proxy.service.base.BaseService;

/**
 * 消息事件分发类
 *
 * @author : cangHX
 * on 2021/05/11  8:46 PM
 */
public interface CloudUtilsEventService extends BaseService {

    /**
     * 绑定事件监听（弱引用）
     *
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 9:22 PM
     */
    void bind(@NonNull CloudMainThreadEventCallback callback);

    /**
     * 绑定事件监听（弱引用）
     *
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 9:22 PM
     */
    void bind(@NonNull CloudWorkThreadEventCallback callback);

    /**
     * 绑定事件监听（生命周期默认与 Activity 相同），建议放在 Activity 的 onStart 或 onCreate 方法中进行绑定
     *
     * @param activity : 上下文，用于提供生命周期，
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 8:57 PM
     */
    void bind(Activity activity, @NonNull CloudMainThreadEventCallback callback);

    /**
     * 绑定事件监听（生命周期默认与 Activity 相同），建议放在 Activity 的 onStart 或 onCreate 方法中进行绑定
     *
     * @param activity : 上下文，用于提供生命周期，
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 8:57 PM
     */
    void bind(Activity activity, @NonNull CloudWorkThreadEventCallback callback);

    /**
     * 绑定事件监听（生命周期默认与 Fragment 相同），建议放在 Fragment 的 onStart 或 onCreate 方法中进行绑定
     *
     * @param fragment : 上下文，用于提供生命周期，
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 8:57 PM
     */
    void bind(Fragment fragment, @NonNull CloudMainThreadEventCallback callback);

    /**
     * 绑定事件监听（生命周期默认与 Fragment 相同），建议放在 Fragment 的 onStart 或 onCreate 方法中进行绑定
     *
     * @param fragment : 上下文，用于提供生命周期，
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 8:57 PM
     */
    void bind(Fragment fragment, @NonNull CloudWorkThreadEventCallback callback);

    /**
     * 移除事件监听
     *
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/17 8:03 PM
     */
    void remove(@NonNull CloudMainThreadEventCallback callback);

    /**
     * 移除事件监听
     *
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/17 8:03 PM
     */
    void remove(@NonNull CloudWorkThreadEventCallback callback);

    /**
     * 同步事件，根据数据类型唤醒符合条件的全部监听
     *
     * @param object : 准备透传的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 9:02 PM
     */
    void set(@NonNull Object object);

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
    void postOrSet(@NonNull Object object);

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
    void postOrSkip(@NonNull Object object);

}
