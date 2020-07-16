package com.proxy.service.ui.uitabhost;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.proxy.service.ui.uitabhost.helper.content.viewpager.ContentViewPagerHelper;
import com.proxy.service.ui.uitabhost.cache.ViewCache;
import com.proxy.service.ui.uitabhost.fragment.PlaceHolderFragment;
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
public class TabHostHelper implements IUiTabHostHelper<TabHostHelper>, TabCallback, ContentCallback, IRewardHelper, ViewCache.ObtainViewListener {

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
    private final List<CloudUiEventCallback> mCloudUiEventCallbacks = new ArrayList<>();
    /**
     * 排序后的数据集合
     */
    private List<IUiTabHostRewardInterface<?>> mTabHostRewardInterfaceList;

    private final String mTag = System.currentTimeMillis() + "_" + Math.random();
    /**
     * 排序后的临时数据
     */
    private List<View> mViewsTemp = null;
    private List<Fragment> mFragmentsTemp = null;

    public TabHostHelper() {
        ViewCache.INSTANCE.addObtainViewListener(this);
    }

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

        Logger.Info("Helper start setActivity. fragmentActivity : " + fragmentActivity);
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

        Logger.Info("Helper start setFragment. fragment : " + fragment);
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
                try {
                    this.mContentHelper = IHelper.create(ContentViewPagerHelper.class);
                } catch (Throwable throwable) {
                    Logger.Error(throwable);
                }
                break;
            case ViewGroupType.LIST_VIEW:
                //TODO 更多viewgroup
                break;
            case ViewGroupType.RECYCLER_VIEW:
                break;
            case ViewGroupType.ERROR:
            default:
                break;
        }

        if (this.mContentHelper == null) {
            this.mContentHelper = new ContentErrorHelper();
            Logger.Error(CloudApiError.UNKNOWN_ERROR.build());
        }
        this.mContentHelper.setContext(this.mContext);
        this.mContentHelper.setFragmentManager(this.mFragmentManager);
        this.mContentHelper.setCallback(this);
        this.mContentHelper.setData(this.mFragmentsTemp);
        this.mContentHelper.setViewGroup(viewGroup);

        Logger.Info("Helper setContentSpace end. ViewGroup : " + viewGroup.toString());
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
                Logger.Error(CloudApiError.DATA_TYPE_ERROR.setMsg("You cannot use viewPager on TabSpace.").build());
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
            Logger.Error(CloudApiError.UNKNOWN_ERROR.build());
        }
        this.mTabHelper.setContext(this.mContext);
        this.mTabHelper.setCallback(this);
        this.mTabHelper.setData(this.mViewsTemp);
        this.mTabHelper.setViewGroup(viewGroup);

        Logger.Info("Helper setTabSpace end. ViewGroup : " + viewGroup.toString());
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
    public TabHostHelper setUiTabHostRewardInterfaces(List<IUiTabHostRewardInterface<?>> list) {
        Logger.Info("Helper start to check data.");

        this.mTabHostRewardInterfaceList = new ArrayList<>(list.size());
        CollectionUtils.fullValue(this.mTabHostRewardInterfaceList, list.size(), null);

        this.mViewsTemp = new ArrayList<>(list.size());
        CollectionUtils.fullValue(this.mViewsTemp, list.size(), null);

        this.mFragmentsTemp = new ArrayList<>(list.size());
        CollectionUtils.fullValue(this.mFragmentsTemp, list.size(), null);

        for (IUiTabHostRewardInterface<?> rewardInterface : list) {
            int index = rewardInterface.getIndex();
            if (index >= list.size()) {
                this.mTabHostRewardInterfaceList.clear();
                this.mViewsTemp.clear();
                this.mFragmentsTemp.clear();
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
            Fragment fragment = object2Fragment(rewardInterface.getContent(), index);
            if (fragment == null) {
                this.mTabHostRewardInterfaceList.clear();
                this.mViewsTemp.clear();
                this.mFragmentsTemp.clear();
                return this;
            }
            this.mFragmentsTemp.set(index, fragment);

            if (this.mSelectIndex == SELECT_NULL && rewardInterface.isDefaultSelect()) {
                this.mSelectIndex = index;
            }
        }
        return this;
    }

    /**
     * 根据 object 内容转换为对应 fragment
     *
     * @param object : 即将转换的 object
     * @return 转换后的 fragment
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/15 10:31 AM
     */
    @Nullable
    private Fragment object2Fragment(Object object, int index) {
        if (object instanceof Fragment) {
            return (Fragment) object;
        } else if (object instanceof View) {
            Bundle bundle = new Bundle();
            bundle.putString(PlaceHolderFragment.TAG, mTag);
            bundle.putInt(PlaceHolderFragment.INDEX, index);
            PlaceHolderFragment fragment = new PlaceHolderFragment();
            fragment.setArguments(bundle);
            return fragment;
        } else {
            Logger.Error(CloudApiError.UNKNOWN_ERROR.append("Discover unknown data. " + object.getClass().getCanonicalName()).build());
        }
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
    @NonNull
    @Override
    public TabHostHelper addEventCallback(CloudUiEventCallback cloudUiEventCallback) {
        if (cloudUiEventCallback != null) {
            Logger.Info("Helper start to addEventCallback. cloudUiEventCallback : " + cloudUiEventCallback);
            this.mCloudUiEventCallbacks.add(cloudUiEventCallback);
        } else {
            Logger.Info("The CloudUiEventCallback cannot be null.");
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
    public synchronized TabHostHelper setSelect(int tabIndex) {
        Logger.Info("Helper start to setSelect. tabIndex : " + tabIndex);
        int index = tabIndex;
        if (!isCanSelect(index)) {
            index = SELECT_NULL;
        }

        if (index < 0 && isLoad) {
            Logger.Warning("This index : " + index + " cannot be selected");
            return this;
        }

        index = index < 0 ? mSelectIndex : index;

        if (index < 0) {
            index = 0;
        }

        if (isLoad) {
            if (!isCanSelect(index)) {
                Logger.Warning("Please check your select index");
                return this;
            }
        } else {
            index = getCanSelectIndex();
            if (index < 0) {
                Logger.Warning("There has no index can select");
                return this;
            }
            Logger.Warning("Intelligent recommendations can be selected index with " + index);
        }

        select(index, TabHostRewardSelectFrom.FROM_HELPER);

        if (mSelectIndex >= 0 && index != mSelectIndex) {
            unSelect(mSelectIndex, TabHostRewardSelectFrom.FROM_HELPER);
        }
        isLoad = true;
        return this;
    }

    /**
     * 刷新数据
     * 针对某些特殊情况，例如：viewpager 默认需要触发滑动才会刷新是否可以选中，
     * 在临界值时会出现第一次无法滑动选中，第二次可以滑动选中
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/16 1:22 PM
     */
    @Override
    public void refresh() {
        if (this.mContentHelper != null) {
            Logger.Info("Helper start to refresh");
            this.mContentHelper.refresh();
        } else {
            Logger.Info(CloudApiError.UNKNOWN_ERROR.build());
        }
    }

    /**
     * 获取一个可以选中的坐标
     *
     * @return 获取到的可以选中的坐标
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/16 1:51 PM
     */
    private int getCanSelectIndex() {
        if (mTabHostRewardInterfaceList.size() <= 0) {
            return -1;
        }

        for (IUiTabHostRewardInterface<?> rewardInterface : mTabHostRewardInterfaceList) {
            if (rewardInterface.isCanSelect()) {
                return rewardInterface.getIndex();
            }
        }

        return -1;
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
        IUiTabHostRewardInterface<?> rewardInterface = mTabHostRewardInterfaceList.get(index);
        if (rewardInterface == null) {
            return false;
        }
        Logger.Info("Helper check can select with " + rewardInterface.getClass().getCanonicalName() + " and index : " + index);
        boolean flag = rewardInterface.isCanSelect();
        if (flag) {
            Logger.Info("Helper find the index : " + index + " can be select");
        } else {
            Logger.Info("Helper find the index : " + index + " cannot be select");
        }
        return flag;
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

        IUiTabHostRewardInterface<?> rewardInterface = mTabHostRewardInterfaceList.get(index);
        try {
            Logger.Info("Helper select with " + rewardInterface.getClass().getCanonicalName());
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
        IUiTabHostRewardInterface<?> rewardInterface = mTabHostRewardInterfaceList.get(index);
        try {
            Logger.Info("Helper unSelect with " + rewardInterface.getClass().getCanonicalName());
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
                Logger.Info("Start get context");
                break;
            case SELECT_INDEX:
                object = mSelectIndex;
                Logger.Info("Start get select index");
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
                Logger.Info("Start send a ui event");
                sendMessage(index, tag);
                break;
            case SELECT_INDEX:
                Logger.Info("Start send a select event");
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

    /**
     * 获取当前接口对应的 tag
     *
     * @return 获取到的 tag
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-12 21:31
     */
    @NonNull
    @Override
    public String getTag() {
        return mTag;
    }

    /**
     * 通过 index 获取对应的view
     *
     * @param index : 位置
     * @return 对应的view
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-12 21:32
     */
    @Override
    public View obtain(int index) {
        if (mTabHostRewardInterfaceList == null) {
            return null;
        }
        if (index < 0 || index >= mTabHostRewardInterfaceList.size()) {
            return null;
        }
        IUiTabHostRewardInterface<?> rewardInterface = mTabHostRewardInterfaceList.get(index);
        if (rewardInterface == null) {
            return null;
        }
        Object object = rewardInterface.getContent();
        if (object instanceof View) {
            return (View) object;
        }
        return null;
    }

    /**
     * 选中进度变化
     *
     * @param index    : 目标下标
     * @param progress : 选中进度
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/16 3:41 PM
     */
    @Override
    public void onSelectProgress(int index, float progress) {
        if (index < 0 || index >= mTabHostRewardInterfaceList.size()) {
            return;
        }
        IUiTabHostRewardInterface<?> rewardInterface = mTabHostRewardInterfaceList.get(index);
        if (rewardInterface == null) {
            return;
        }
        try {
            rewardInterface.onSelectProgress(progress);
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
    }
}
