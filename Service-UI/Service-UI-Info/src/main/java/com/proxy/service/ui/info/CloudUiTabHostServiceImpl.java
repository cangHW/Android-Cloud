package com.proxy.service.ui.info;

import com.proxy.service.annotations.CloudNewInstance;
import com.proxy.service.annotations.CloudService;
import com.proxy.service.api.callback.CloudUiEventCallback;
import com.proxy.service.api.services.CloudUiTabHostService;
import com.proxy.service.api.tag.CloudServiceTagUi;

/**
 * @author: cangHX
 * on 2020/06/29  14:57
 */
@CloudNewInstance
@CloudService(serviceTag = CloudServiceTagUi.UI_TAB_HOST)
public class CloudUiTabHostServiceImpl implements CloudUiTabHostService {
    /**
     * 设置内容区域
     *
     * @param layoutId : ViewGroup id，用于展示内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 09:33
     */
    @Override
    public CloudUiTabHostService setContentSpace(int layoutId) {
        return null;
    }

    /**
     * 设置tab区域
     *
     * @param layoutId : ViewGroup id，用于展示tab
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 09:35
     */
    @Override
    public CloudUiTabHostService setTabSpace(int layoutId) {
        return null;
    }

    /**
     * 添加事件回调
     *
     * @param cloudUiEventCallback : 用于接收事件回调的监听
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:10
     */
    @Override
    public CloudUiTabHostService addEventCallback(CloudUiEventCallback cloudUiEventCallback) {
        return null;
    }

    /**
     * 设置选中的tab
     *
     * @param tabIndex : 用于标示tab
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:19
     */
    @Override
    public CloudUiTabHostService setSelect(int tabIndex) {
        return null;
    }

    /**
     * 开始展示ui
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:21
     */
    @Override
    public void show() {

    }

    /**
     * 开始展示ui
     *
     * @param rewardTag : 用于筛选将要进行展示的ui
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:21
     */
    @Override
    public void showWithTag(String rewardTag) {

    }
}
