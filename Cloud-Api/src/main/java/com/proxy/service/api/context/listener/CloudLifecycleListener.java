package com.proxy.service.api.context.listener;

import android.app.Activity;

import com.proxy.service.api.context.LifecycleState;

/**
 * @author : cangHX
 * on 2020/07/15  2:11 PM
 */
public interface CloudLifecycleListener {

    /**
     * 回调生命周期
     *
     * @param activity       : 准备观察生命周期的对象
     * @param lifecycleState : 生命周期状态回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/07/15  2:11 PM
     */
    void onLifecycleChanged(Activity activity, LifecycleState lifecycleState);
}
