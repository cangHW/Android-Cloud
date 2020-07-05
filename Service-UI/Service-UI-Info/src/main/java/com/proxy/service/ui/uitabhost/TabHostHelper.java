package com.proxy.service.ui.uitabhost;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.proxy.service.api.annotations.TabHostRewardSelectFrom;
import com.proxy.service.api.callback.CloudUiEventCallback;
import com.proxy.service.api.callback.CloudUiLifeCallback;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.interfaces.IUiTabHostHelper;
import com.proxy.service.api.interfaces.IUiTabHostRewardInterface;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.ui.annotations.ViewGroupType;
import com.proxy.service.ui.uitabhost.helper.base.IHelper;
import com.proxy.service.ui.uitabhost.helper.content.base.IContentHelper;
import com.proxy.service.ui.uitabhost.helper.content.error.ContentErrorHelper;
import com.proxy.service.ui.uitabhost.helper.content.normal.ContentNormalHelper;
import com.proxy.service.ui.uitabhost.helper.tab.base.ITabHelper;
import com.proxy.service.ui.uitabhost.helper.tab.error.TabErrorHelper;
import com.proxy.service.ui.uitabhost.helper.tab.normal.TabNormalHelper;
import com.proxy.service.ui.uitabhost.listener.ContentCallback;
import com.proxy.service.ui.uitabhost.listener.TabCallback;
import com.proxy.service.ui.util.CollectionUtils;
import com.proxy.service.ui.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/30  18:50
 * <p>
 * 中转站，关联 helper 与 IUiTabHostRewardInterface
 */
public class TabHostHelper implements IUiTabHostHelper<TabHostHelper>, TabCallback, ContentCallback {

    /**
     * 没有选中或者传入数据有误
     */
    public static final int SELECT_NULL = -1;

    /**
     * 是否加载过
     */
    private boolean isLoad = false;
    private int mSelectIndex = SELECT_NULL;
    private IContentHelper mContentHelper = null;
    private ITabHelper mTabHelper = null;
    private List<CloudUiEventCallback> mCloudUiEventCallbacks = new ArrayList<>();
    private List<IUiTabHostRewardInterface> mTabHostRewardInterfaceList;

    /**
     * 设置依赖的 Activity
     * <p>
     * 两个方法执行一个即可，{@link IUiTabHostHelper#setFragment(Fragment)}
     *
     * @param fragmentActivity : 依赖的 Activity
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-30 18:22
     */
    @NonNull
    @Override
    public TabHostHelper setActivity(FragmentActivity fragmentActivity) {
        if (fragmentActivity != null) {
            this.mContentHelper.setFragmentManager(fragmentActivity.getSupportFragmentManager());
            this.mContentHelper.setContext(fragmentActivity);
            this.mTabHelper.setContext(fragmentActivity);
        }
        return this;
    }

    /**
     * 设置依赖的 Fragment
     * <p>
     * 两个方法执行一个即可，{@link IUiTabHostHelper#setActivity(FragmentActivity)}
     *
     * @param fragment : 依赖的 Fragment
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-30 18:24
     */
    @NonNull
    @Override
    public TabHostHelper setFragment(Fragment fragment) {
        if (fragment != null) {
            this.mContentHelper.setFragmentManager(fragment.getChildFragmentManager());
            this.mContentHelper.setContext(fragment.getContext());
            this.mTabHelper.setContext(fragment.getContext());
        }
        return this;
    }

    /**
     * 设置内容区域
     * 如果是 viewpager 或者 viewpager2 建议实现对应接口，方便获取更加准确的生命周期
     * 接口请看{@link CloudUiLifeCallback}
     *
     * @param viewGroup : ViewGroup，用于展示内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 09:33
     */
    @NonNull
    @Override
    public TabHostHelper setContentSpace(ViewGroup viewGroup) {
        viewGroup.removeAllViews();
        int type = ViewUtils.getViewGroupType(viewGroup);
        switch (type) {
            case ViewGroupType.SCROLL_VIEW:
            case ViewGroupType.HORIZONTAL_SCROLL_VIEW:
                break;
            case ViewGroupType.FRAME_LAYOUT:
            case ViewGroupType.LINEAR_LAYOUT:
            case ViewGroupType.RELATIVE_LAYOUT:
            case ViewGroupType.LINEAR_LAYOUT_COMPAT:
                try {
                    this.mContentHelper = IHelper.create(ContentNormalHelper.class);
                } catch (Throwable throwable) {
                    Logger.Error(throwable);
                }
                break;
            case ViewGroupType.VIEW_PAGER:
                break;
            case ViewGroupType.LIST_VIEW:
                break;
            case ViewGroupType.RECYCLER_VIEW:
                break;
            case ViewGroupType.ERROR:
            default:
                break;
        }

        if (this.mContentHelper == null) {
            this.mContentHelper = new ContentErrorHelper();
        }
        this.mContentHelper.setViewGroup(viewGroup);
        return this;
    }

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
    public TabHostHelper setTabSpace(ViewGroup viewGroup) {
        viewGroup.removeAllViews();
        int type = ViewUtils.getViewGroupType(viewGroup);
        switch (type) {
            case ViewGroupType.SCROLL_VIEW:
            case ViewGroupType.HORIZONTAL_SCROLL_VIEW:
                break;
            case ViewGroupType.FRAME_LAYOUT:
            case ViewGroupType.LINEAR_LAYOUT:
            case ViewGroupType.RELATIVE_LAYOUT:
            case ViewGroupType.LINEAR_LAYOUT_COMPAT:
                try {
                    this.mTabHelper = IHelper.create(TabNormalHelper.class);
                } catch (Throwable throwable) {
                    Logger.Error(throwable);
                }
                break;
            case ViewGroupType.VIEW_PAGER:
                break;
            case ViewGroupType.LIST_VIEW:
                break;
            case ViewGroupType.RECYCLER_VIEW:
                break;
            case ViewGroupType.ERROR:
            default:
                break;
        }

        if (this.mTabHelper == null) {
            this.mTabHelper = new TabErrorHelper();
        }
        this.mTabHelper.setViewGroup(viewGroup);
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
    public TabHostHelper setUiTabHostRewardInterfaces(List<IUiTabHostRewardInterface> list) {
        this.mTabHostRewardInterfaceList = new ArrayList<>(list.size());
        CollectionUtils.fullValue(this.mTabHostRewardInterfaceList, list.size(), null);

        List<View> views = new ArrayList<>(list.size());
        CollectionUtils.fullValue(views, list.size(), null);

        List<Object> objects = new ArrayList<>(list.size());
        CollectionUtils.fullValue(objects, list.size(), null);

        for (IUiTabHostRewardInterface rewardInterface : list) {
            Object object = this.mTabHostRewardInterfaceList.get(rewardInterface.getIndex());
            if (object != null) {
                this.mTabHostRewardInterfaceList.clear();
                Logger.Error(CloudApiError.DATA_DUPLICATION.append("Repeat for the " + object.getClass().getCanonicalName() + " and " + rewardInterface.getClass().getCanonicalName() + " index").build());
                return this;
            }
            this.mTabHostRewardInterfaceList.set(rewardInterface.getIndex(), rewardInterface);
            views.set(rewardInterface.getIndex(), rewardInterface.getTab());
            objects.set(rewardInterface.getIndex(), rewardInterface.getContent());

            if (this.mSelectIndex == SELECT_NULL && rewardInterface.isDefaultSelect()) {
                this.mSelectIndex = rewardInterface.getIndex();
            }
        }

        this.mTabHelper.setData(views);
        this.mContentHelper.setData(objects);

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
    public TabHostHelper addEventCallback(CloudUiEventCallback cloudUiEventCallback) {
        if (cloudUiEventCallback != null) {
            this.mCloudUiEventCallbacks.add(cloudUiEventCallback);
        }
        this.mContentHelper.setCallback(this);
        this.mTabHelper.setCallback(this);
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
    public TabHostHelper setSelect(int tabIndex) {
        int index = tabIndex;
        if (!isCanSelect(index)) {
            index = SELECT_NULL;
        }

        if (index < 0 && isLoad) {
            return this;
        }

        isLoad = true;

        index = index < 0 ? mSelectIndex : index;

        if (index < 0) {
            index = 0;
        }

        if (!isCanSelect(index)) {
            return this;
        }

        select(index, TabHostRewardSelectFrom.FROM_HELPER);

        if (mSelectIndex >= 0 && index != mSelectIndex) {
            unSelect(mSelectIndex, TabHostRewardSelectFrom.FROM_HELPER);
        }

        return this;
    }

    /**
     * 目标是否可以被选中
     *
     * @param index : 选中的 index
     * @return true 可以选中，false 不可以选中
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-05 08:47
     */
    @Override
    public boolean isCanSelect(int index) {
        if (index < 0 || index >= mTabHostRewardInterfaceList.size()) {
            return false;
        }
        IUiTabHostRewardInterface rewardInterface = mTabHostRewardInterfaceList.get(index);
        if (rewardInterface == null) {
            return false;
        }
        return rewardInterface.isCanSelect();
    }

    /**
     * 选中
     *
     * @param index : 选中的 index
     * @param from  : 事件来源
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-03 10:44
     */
    @Override
    public void select(int index, String from) {
        switch (from) {
            case TabHostRewardSelectFrom.FROM_CONTENT:
                this.mTabHelper.setSelect(index, from);
                break;
            case TabHostRewardSelectFrom.FROM_HELPER:
                this.mTabHelper.setSelect(index, from);
                this.mContentHelper.setSelect(index, from);
                break;
            case TabHostRewardSelectFrom.FROM_TAB:
                this.mContentHelper.setSelect(index, from);
                break;
            default:
                break;
        }

        IUiTabHostRewardInterface rewardInterface = mTabHostRewardInterfaceList.get(index);
        try {
            rewardInterface.onSelect(from);
        } catch (Throwable throwable) {
            Logger.Error(throwable);
        }
        this.mSelectIndex = index;
    }

    /**
     * 取消选中
     *
     * @param index : 取消选中的 index
     * @param from  : 事件来源
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-03 17:31
     */
    @Override
    public void unSelect(int index, String from) {
        IUiTabHostRewardInterface rewardInterface = mTabHostRewardInterfaceList.get(index);
        try {
            rewardInterface.onUnSelect(from);
        } catch (Throwable throwable) {
            Logger.Error(throwable);
        }
    }
}
