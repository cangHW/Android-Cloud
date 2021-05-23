package com.proxy.service.api.lifecycle;

import androidx.fragment.app.Fragment;

/**
 * @author : cangHX
 * on 2021/05/20  9:56 PM
 */
public interface CloudFragmentLifecycleListener {

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
