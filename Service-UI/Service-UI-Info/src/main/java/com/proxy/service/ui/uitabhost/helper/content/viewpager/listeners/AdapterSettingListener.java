package com.proxy.service.ui.uitabhost.helper.content.viewpager.listeners;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author : cangHX
 * on 2020/07/14  6:38 PM
 */
public interface AdapterSettingListener {

    /**
     * 获取 fragment
     *
     * @param position : 目标位置
     * @return 获取到的 fragment
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/14 6:51 PM
     */
    @Nullable
    Fragment getFragment(int position);

    /**
     * 添加 fragment 并刷新页面
     *
     * @param index    : 目标位置
     * @param fragment : 准备添加的 fragment
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/14 6:52 PM
     */
    void addFragment(int index, @NonNull Fragment fragment);

    /**
     * 移除 fragment 并刷新页面
     *
     * @param index    : 目标位置
     * @param fragment : 准备移除的 fragment
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/14 6:52 PM
     */
    void removeFragment(int index, @NonNull Fragment fragment);

}
