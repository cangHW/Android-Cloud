package com.proxy.service.ui.uitabhost.helper.content.viewpager.listeners;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

/**
 * @author : cangHX
 * on 2020/07/14  6:38 PM
 */
public interface AdapterSettingListener {

    /**
     * 查询目标在数组中的位置
     *
     * @param fragment : 准备查询的目标
     * @return 获取到的位置
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/15 10:26 PM
     */
    int getIndex(Fragment fragment);

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
     * 检查 fragment 数组，并按需刷新页面
     *
     * @param fragments : 发生改变后的 fragment 数组
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/14 6:52 PM
     */
    void changeFragment(@NonNull List<Fragment> fragments);

}
