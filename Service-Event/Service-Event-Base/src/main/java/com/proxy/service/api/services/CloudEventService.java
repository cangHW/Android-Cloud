package com.proxy.service.api.services;

import android.app.Activity;
import android.app.Fragment;

import com.proxy.service.api.event.CloudEventCallback;
import com.proxy.service.api.event.CloudMainThreadEventCallback;
import com.proxy.service.api.event.CloudWorkThreadEventCallback;
import com.proxy.service.base.BaseService;

/**
 * @author : cangHX
 * on 2021/05/11  8:46 PM
 */
public interface CloudEventService extends BaseService {

    /**
     * 绑定事件监听
     *
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 9:22 PM
     */
    <T> void bind(CloudEventCallback<T> callback);

    /**
     * 绑定事件监听
     *
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 9:22 PM
     */
    <T> void bind(CloudMainThreadEventCallback<T> callback);

    /**
     * 绑定事件监听
     *
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 9:22 PM
     */
    <T> void bind(CloudWorkThreadEventCallback<T> callback);

    /**
     * 绑定事件监听
     *
     * @param activity : 上下文，用于提供生命周期，
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 8:57 PM
     */
    <T> void bind(Activity activity, CloudEventCallback<T> callback);

    /**
     * 绑定事件监听
     *
     * @param activity : 上下文，用于提供生命周期，
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 8:57 PM
     */
    <T> void bind(Activity activity, CloudMainThreadEventCallback<T> callback);

    /**
     * 绑定事件监听
     *
     * @param activity : 上下文，用于提供生命周期，
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 8:57 PM
     */
    <T> void bind(Activity activity, CloudWorkThreadEventCallback<T> callback);

    /**
     * 绑定事件监听
     *
     * @param fragment : 上下文，用于提供生命周期，
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 8:57 PM
     */
    <T> void bind(Fragment fragment, CloudEventCallback<T> callback);

    /**
     * 绑定事件监听
     *
     * @param fragment : 上下文，用于提供生命周期，
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 8:57 PM
     */
    <T> void bind(Fragment fragment, CloudMainThreadEventCallback<T> callback);

    /**
     * 绑定事件监听
     *
     * @param fragment : 上下文，用于提供生命周期，
     * @param callback : 事件回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 8:57 PM
     */
    <T> void bind(Fragment fragment, CloudWorkThreadEventCallback<T> callback);

    /**
     * 同步事件，根据数据类型唤醒符合条件的全部监听
     *
     * @param t : 准备透传的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 9:02 PM
     */
    <T> void set(T t);

    /**
     * 异步事件，根据数据类型唤醒符合条件的全部监听（如果对应监听未与生命周期绑定，则会被切换为同步事件）
     *
     * @param t : 准备透传的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 9:04 PM
     */
    <T> void post(T t);

    /**
     * 异步事件，根据数据类型唤醒符合条件的全部监听（如果对应监听未与生命周期绑定，则会被跳过）
     *
     * @param t : 准备透传的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/14 9:04 PM
     */
    <T> void postOrSkip(T t);

}
