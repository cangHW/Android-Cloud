package com.proxy.service.ui.info;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.proxy.service.annotations.CloudApiNewInstance;
import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.annotations.CloudUiTabHostReward;
import com.proxy.service.api.callback.CloudUiEventCallback;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.interfaces.IUiTabHostHelper;
import com.proxy.service.api.interfaces.IUiTabHostRewardInterface;
import com.proxy.service.api.service.OtherManager;
import com.proxy.service.api.services.CloudUiTabHostService;
import com.proxy.service.api.tag.CloudServiceTagUi;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.ui.uitabhost.TabHostHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/29  14:57
 */
@CloudApiNewInstance
@CloudApiService(serviceTag = CloudServiceTagUi.UI_TAB_HOST)
public class UiTabHostServiceImpl implements CloudUiTabHostService {

    private IUiTabHostHelper<TabHostHelper> mTabHostHelper;
    private FragmentActivity mFragmentActivity;
    private Fragment mFragment;
    private ViewGroup mContentViewGroup;
    private ViewGroup mTabViewGroup;
    private CloudUiEventCallback mCloudUiEventCallback;
    private int mTabIndex = -1;

    /**
     * 是否加载过 ui
     */
    private boolean isLoadUi = false;

    /**
     * 设置依赖的 Activity
     * <p>
     * 两个方法执行一个即可，{@link CloudUiTabHostService#setFragment(Fragment)}
     *
     * @param fragmentActivity : 依赖的 Activity
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-30 18:22
     */
    @NonNull
    @Override
    public CloudUiTabHostService setActivity(FragmentActivity fragmentActivity) {
        this.mFragmentActivity = fragmentActivity;
        return this;
    }

    /**
     * 设置依赖的 Fragment
     * <p>
     * 两个方法执行一个即可，{@link CloudUiTabHostService#setActivity(FragmentActivity)}
     *
     * @param fragment : 依赖的 Fragment
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-30 18:24
     */
    @NonNull
    @Override
    public CloudUiTabHostService setFragment(Fragment fragment) {
        this.mFragment = fragment;
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
    public CloudUiTabHostService setContentSpace(ViewGroup viewGroup) {
        this.mContentViewGroup = viewGroup;
        if (!isLoadUi) {
            mTabHostHelper = new TabHostHelper();
        }
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
    public CloudUiTabHostService setTabSpace(ViewGroup viewGroup) {
        this.mTabViewGroup = viewGroup;
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
    public CloudUiTabHostService addEventCallback(CloudUiEventCallback cloudUiEventCallback) {
        if (isLoadUi) {
            this.mTabHostHelper.addEventCallback(cloudUiEventCallback);
        } else {
            this.mCloudUiEventCallback = cloudUiEventCallback;
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
    public CloudUiTabHostService setSelect(int tabIndex) {
        if (isLoadUi) {
            this.mTabHostHelper.setSelect(tabIndex);
        } else {
            this.mTabIndex = tabIndex;
        }
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
        if (mTabHostHelper != null) {
            mTabHostHelper.refresh();
        }
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
        if (isLoadUi) {
            return;
        }
        show("cloud_reward_normal");
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
    public void showWithTag(@NonNull String rewardTag) {
        if (isLoadUi) {
            return;
        }
        show(rewardTag);
    }

    /**
     * 开始加载ui视图
     *
     * @param rewardTag : 用于筛选将要进行展示的ui
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-01 10:38
     */
    private void show(String rewardTag) {
        if (!check()) {
            return;
        }
        isLoadUi = true;

        mTabHostHelper.setFragment(mFragment)
                .setActivity(mFragmentActivity)
                .addEventCallback(mCloudUiEventCallback)
                .setUiTabHostRewardInterfaces(getData(rewardTag))
                .setContentSpace(mContentViewGroup)
                .setTabSpace(mTabViewGroup)
                .setSelect(mTabIndex);
    }

    /**
     * 检查数据是否完整
     *
     * @return true 完整，可以加载 UI。   false 不完整
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-01 10:48
     */
    private boolean check() {
        if (mContentViewGroup == null) {
            Logger.Error(CloudApiError.DATA_EMPTY.setMsg("The ContentSpace is null").build());
            return false;
        }
        if (mTabViewGroup == null) {
            Logger.Error(CloudApiError.DATA_EMPTY.setMsg("The TabSpace is null").build());
            return false;
        }
        if (mFragmentActivity == null && mFragment == null) {
            Logger.Error(CloudApiError.DATA_EMPTY.setMsg("The Activity or Fragment is null").build());
            return false;
        }
        return true;
    }

    /**
     * 通过 rewardTag 获取对应的数据
     *
     * @param rewardTag : 标记 tag
     * @return 获取到的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-01 09:50
     */
    private List<IUiTabHostRewardInterface<?>> getData(@NonNull String rewardTag) {
        List<IUiTabHostRewardInterface<?>> list = new ArrayList<>();

        List<Object> objectList = OtherManager.INSTANCE.getOtherObjectByAnnotation(CloudUiTabHostReward.class);
        for (Object object : objectList) {

            boolean flag = object instanceof IUiTabHostRewardInterface;

            if (!flag) {
                Logger.Error(CloudApiError.DATA_TYPE_ERROR.setMsg("Are you sure " + object.getClass().getCanonicalName() + " inherits IUiTabHostRewardInterface?").build());
                list.clear();
                return list;
            }

            if ("".equals(rewardTag)) {
                list.add((IUiTabHostRewardInterface<?>) object);
                continue;
            }

            CloudUiTabHostReward reward = object.getClass().getAnnotation(CloudUiTabHostReward.class);
            if (reward == null) {
                continue;
            }

            String tag = reward.rewardTag();
            if (rewardTag.equals(tag)) {
                list.add((IUiTabHostRewardInterface<?>) object);
            }
        }

        if (list.size() == 0) {
            Logger.Debug(CloudApiError.DATA_EMPTY.append("Are you sure you added CloudUiTabHostReward annotation?").build());
        }

        return list;
    }
}
