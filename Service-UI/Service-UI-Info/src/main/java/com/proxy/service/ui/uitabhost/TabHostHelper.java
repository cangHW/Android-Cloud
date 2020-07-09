package com.proxy.service.ui.uitabhost;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.proxy.service.api.annotations.TabHostRewardSelectFrom;
import com.proxy.service.api.callback.CloudUiEventCallback;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.interfaces.IRewardHelper;
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
 * 中转站，关联 helper 与 IUiTabHostRewardInterface
 *
 * @author: cangHX
 * on 2020/06/30  18:50
 */
public class TabHostHelper implements IUiTabHostHelper<TabHostHelper>, TabCallback, ContentCallback, IRewardHelper {

    /**
     * 没有选中或者传入数据有误
     */
    public static final int SELECT_NULL = -1;

    /**
     * 是否加载过
     */
    private boolean isLoad = false;

    /**
     * 当前选中
     */
    private int mSelectIndex = SELECT_NULL;
    /**
     * content 辅助类
     */
    private IContentHelper mContentHelper = null;
    /**
     * tab 辅助类
     */
    private ITabHelper mTabHelper = null;

    private Context mContext;
    private FragmentManager mFragmentManager;

    /**
     * Event 事件回调集合
     */
    private List<CloudUiEventCallback> mCloudUiEventCallbacks = new ArrayList<>();
    /**
     * 数据集合
     */
    private List<IUiTabHostRewardInterface> mTabHostRewardInterfaceList;

    /**
     * 临时数据
     */
    private List<View> mViewsTemp = null;
    private List<Object> mObjectsTemp = null;

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
        if (fragmentActivity == null) {
            return this;
        }
        this.mContext = fragmentActivity;
        this.mFragmentManager = fragmentActivity.getSupportFragmentManager();
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
        if (fragment == null) {
            return this;
        }
        this.mContext = fragment.getContext();
        this.mFragmentManager = fragment.getChildFragmentManager();
        return this;
    }

    /**
     * 设置内容区域
     * 如果是 viewpager 或者 viewpager2 建议实现对应接口，方便获取更加准确的生命周期
     * 接口请看{@link com.proxy.service.api.callback.CloudUiLifeCallback}
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
        if (isLoad) {
            return this;
        }
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
        this.mContentHelper.setContext(this.mContext);
        this.mContentHelper.setFragmentManager(this.mFragmentManager);
        this.mContentHelper.setCallback(this);
        this.mContentHelper.setData(this.mObjectsTemp);
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
        if (isLoad) {
            return this;
        }
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
        this.mTabHelper.setContext(this.mContext);
        this.mTabHelper.setCallback(this);
        this.mTabHelper.setData(this.mViewsTemp);
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

        this.mViewsTemp = new ArrayList<>(list.size());
        CollectionUtils.fullValue(this.mViewsTemp, list.size(), null);

        this.mObjectsTemp = new ArrayList<>(list.size());
        CollectionUtils.fullValue(this.mObjectsTemp, list.size(), null);

        for (IUiTabHostRewardInterface rewardInterface : list) {
            int index = rewardInterface.getIndex();
            if (index >= list.size()) {
                Logger.Error(rewardInterface.getClass().getCanonicalName() + ": position is error,The total count: " + list.size() + ",The position: " + index);
                return this;
            }

            Object object = this.mTabHostRewardInterfaceList.get(index);
            if (object != null) {
                this.mTabHostRewardInterfaceList.clear();
                Logger.Error(CloudApiError.DATA_DUPLICATION.append("Repeat for the " + object.getClass().getCanonicalName() + " and " + rewardInterface.getClass().getCanonicalName() + " index").build());
                return this;
            }
            rewardInterface.setRewardHelper(this);
            this.mTabHostRewardInterfaceList.set(index, rewardInterface);
            this.mViewsTemp.set(index, rewardInterface.getTab());
            this.mObjectsTemp.set(index, rewardInterface.getContent());

            if (this.mSelectIndex == SELECT_NULL && rewardInterface.isDefaultSelect()) {
                this.mSelectIndex = index;
            }
        }
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

    /**
     * 根据标签获取数据
     *
     * @param helper : 标签
     * @return 获取的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-06 13:45
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Get helper) {
        Object object;
        switch (helper) {
            case CONTEXT:
                object = mContext;
                break;
            case SELECT_INDEX:
                object = mSelectIndex;
                break;
            default:
                object = null;
                break;
        }
        try {
            return (T) object;
        } catch (Throwable throwable) {
            Logger.Error(throwable);
        }
        return null;
    }

    /**
     * 根据标签进行事件发送
     *
     * @param helper : 标签
     * @param index  : 页面的 index，PS：如果是跳转事件{@link Set#SELECT_INDEX}，则 index 为目标页面的 index
     * @param tag    : 保留字段，开发者可以自定义使用
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-06 13:46
     */
    @Override
    public void set(Set helper, int index, Object tag) {
        switch (helper) {
            case UI_EVENT:
                sendMessage(index, tag);
                break;
            case SELECT_INDEX:
                setSelect(index);
                break;
            default:
                break;
        }
    }

    private void sendMessage(int index, Object tag) {
        for (CloudUiEventCallback callback : mCloudUiEventCallbacks) {
            if (callback == null) {
                continue;
            }
            try {
                callback.onCall(index, tag);
            } catch (Throwable throwable) {
                Logger.Error(throwable);
            }
        }
    }
}
