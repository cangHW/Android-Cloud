package com.proxy.service.api.services;

import androidx.annotation.NonNull;

import com.proxy.service.api.base.CloudUiTabHostFragmentReward;
import com.proxy.service.api.interfaces.IUiTabHostHelper;

/**
 * android 选项卡类型页面，经典场景--首页，tab切换页面
 * <p>
 * 对应模块需要实现相关功能
 * <p>
 * {@link CloudUiTabHostFragmentReward}
 * <p>
 * 主要通过相关子类来实现具体的ui效果
 *
 * @author: cangHX
 * on 2020/06/28  21:01
 */
public interface CloudUiTabHostService extends IUiTabHostHelper<CloudUiTabHostService> {

    /**
     * 开始展示ui
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:21
     */
    void show();

    /**
     * 开始展示ui
     *
     * @param rewardTag : 用于筛选将要进行展示的ui
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:21
     */
    void showWithTag(@NonNull String rewardTag);
}
