package com.proxy.service.api.services;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.proxy.service.api.context.LifecycleState;
import com.proxy.service.api.lifecycle.CloudActivityLifecycleListener;
import com.proxy.service.api.lifecycle.CloudFragmentLifecycleListener;
import com.proxy.service.api.lifecycle.FragmentLifecycleState;
import com.proxy.service.base.BaseService;

/**
 * 生命周期监听类
 *
 * @author : cangHX
 * on 2021/05/20  9:28 PM
 */
public interface CloudUtilsLifecycleService extends BaseService {

    /**
     * 绑定 Activity 的生命周期
     *
     * @param activity          : 上下文
     * @param lifecycleListener : 生命周期回调
     * @param lifecycleState    : 声明监听的生命周期
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 9:29 PM
     */
    void bind(Activity activity, CloudActivityLifecycleListener lifecycleListener, LifecycleState... lifecycleState);

    /**
     * 绑定 Fragment 的生命周期
     *
     * @param fragment          : 上下文
     * @param lifecycleListener : 生命周期回调
     * @param lifecycleState    : 声明监听的生命周期
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 9:29 PM
     */
    void bind(Fragment fragment, CloudFragmentLifecycleListener lifecycleListener, FragmentLifecycleState... lifecycleState);

    /**
     * 移除 Activity 的生命周期监听
     *
     * @param activity : 上下文
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 9:29 PM
     */
    void remove(Activity activity);

    /**
     * 移除 Activity 的生命周期监听
     *
     * @param lifecycleListener : 生命周期回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 9:29 PM
     */
    void remove(CloudActivityLifecycleListener lifecycleListener);

    /**
     * 移除 Fragment 的生命周期监听
     *
     * @param fragment : 上下文
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 9:29 PM
     */
    void remove(Fragment fragment);

    /**
     * 移除 Fragment 的生命周期监听
     *
     * @param lifecycleListener : 生命周期回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/5/20 9:29 PM
     */
    void remove(CloudFragmentLifecycleListener lifecycleListener);
}
