package com.proxy.service.utils.lifecycle;

import androidx.fragment.app.Fragment;

import com.proxy.service.api.lifecycle.FragmentLifecycleState;

/**
 * @author : cangHX
 * on 2021/05/20  9:42 PM
 */
public interface LifecycleFragmentListener {

    /**
     * 回调生命周期
     *
     * @param fragment       : 准备观察生命周期的对象
     * @param lifecycleState : 生命周期状态回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/05/20  9:56 PM
     */
    void onLifecycleChanged(Fragment fragment, FragmentLifecycleState lifecycleState);

}
