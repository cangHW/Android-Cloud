package com.proxy.service.ui.uitabhost.base;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.CloudUiEventCallback;
import com.proxy.service.api.interfaces.IUiTabHostHelper;
import com.proxy.service.api.interfaces.IUiTabHostRewardInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/30  18:50
 */
public abstract class AbstractTabHostHelper implements IUiTabHostHelper<AbstractTabHostHelper> {

    protected int mSelectIndex = -1;
    protected List<CloudUiEventCallback> mCloudUiEventCallbacks = new ArrayList<>();
    protected List<IUiTabHostRewardInterface> mTabHostRewardInterfaceList;

    /**
     * 设置tab区域
     *
     * @param viewGroup : ViewGroup，用于展示tab
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 09:35
     */
    @NonNull
    @Override
    public AbstractTabHostHelper setTabSpace(ViewGroup viewGroup) {
        viewGroup.removeAllViews();




        return this;
    }

    /**
     * 设置数据
     *
     * @param list : 数据集合
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-01 10:08
     */
    public AbstractTabHostHelper setUiTabHostRewardInterfaces(List<IUiTabHostRewardInterface> list) {
        this.mTabHostRewardInterfaceList = list;
        return this;
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
    @NonNull
    @Override
    public AbstractTabHostHelper addEventCallback(CloudUiEventCallback cloudUiEventCallback) {
        this.mCloudUiEventCallbacks.add(cloudUiEventCallback);
        return this;
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
    @NonNull
    @Override
    public AbstractTabHostHelper setSelect(int tabIndex) {
        return this;
    }

    /**
     * 修改选中
     *
     * @param now : 当前选中
     * @param old : 之前选中
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-01 10:25
     */
    protected abstract void changeSelect(int now, int old);
}
