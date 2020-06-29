package com.proxy.service.api.services;

import androidx.annotation.LayoutRes;

import com.proxy.service.api.callback.CloudUiEventCallback;
import com.proxy.service.base.BaseService;

/**
 * @author: cangHX
 * on 2020/06/28  21:01
 * <p>
 * android 选项卡类型页面，经典场景--首页，tab切换页面
 */
public interface CloudUiTabHostService extends BaseService {

    /**
     * 设置内容区域
     *
     * @param layoutId : ViewGroup id，用于展示内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 09:33
     */
    CloudUiTabHostService setContentSpace(@LayoutRes int layoutId);

    /**
     * 设置tab区域
     *
     * @param layoutId : ViewGroup id，用于展示tab
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 09:35
     */
    CloudUiTabHostService setTabSpace(@LayoutRes int layoutId);

    /**
     * 添加事件回调
     *
     * @param cloudUiEventCallback : 用于接收事件回调的监听
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:10
     */
    CloudUiTabHostService addEventCallback(CloudUiEventCallback cloudUiEventCallback);

    /**
     * 设置选中的tab
     *
     * @param tabIndex : 用于标示tab
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:19
     */
    CloudUiTabHostService setSelect(int tabIndex);

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
    void showWithTag(String rewardTag);
}
