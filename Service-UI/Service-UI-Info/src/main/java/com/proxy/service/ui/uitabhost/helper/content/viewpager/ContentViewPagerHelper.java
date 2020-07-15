package com.proxy.service.ui.uitabhost.helper.content.viewpager;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.proxy.service.api.annotations.TabHostRewardSelectFrom;
import com.proxy.service.api.callback.CloudUiLifeCallback;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.context.listener.CloudLifecycleListener;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.ui.uitabhost.helper.content.base.AbstractContentHelper;
import com.proxy.service.ui.uitabhost.helper.content.viewpager.adapter.ContentFragmentPagerAdapter;
import com.proxy.service.ui.uitabhost.helper.content.viewpager.adapter.ContentFragmentStatePagerAdapter;
import com.proxy.service.ui.uitabhost.helper.content.viewpager.listeners.AdapterSettingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/07/12  18:20
 */
public class ContentViewPagerHelper extends AbstractContentHelper implements ViewPager.OnPageChangeListener {

    /**
     * 未滑动
     */
    private static final int SCROLL_IDLE = 0;
    /**
     * 滑动中
     */
    private static final int SCROLL_PROGRESS = 1;
    /**
     * viewpager 滑动方向计算的中位数
     */
    private static final float DIRECTION_MEDIAN = 0.5f;
    /**
     * 切换 adapter 的临界值
     */
    private static final int MAX = 5;

    private ViewPager mViewPager;
    private AdapterSettingListener mAdapterSetting;
    private int mScrollState = SCROLL_IDLE;

    private CloudLifecycleListener mLifecycleListener = new CloudLifecycleListener() {
        @Override
        public void onActivityResumed(Activity activity) {
            if (mSelect < 0 || mSelect >= mList.size()) {
                return;
            }
            Fragment fragment = mList.get(mSelect);
            if (fragment instanceof CloudUiLifeCallback) {
                CloudUiLifeCallback uiLifeCallback = (CloudUiLifeCallback) fragment;
                uiLifeCallback.onUiResume();
                uiLifeCallback.onUiVisible();
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (mSelect < 0 || mSelect >= mList.size()) {
                return;
            }
            Fragment fragment = mList.get(mSelect);
            if (fragment instanceof CloudUiLifeCallback) {
                CloudUiLifeCallback uiLifeCallback = (CloudUiLifeCallback) fragment;
                uiLifeCallback.onUiStop();
                uiLifeCallback.onUiInVisible();
            }
        }
    };

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            ContextManager.addLifecycleListener(activity, mLifecycleListener);
        }
    }

    /**
     * 切换选中
     *
     * @param old  : 当前选中
     * @param now  : 即将选中
     * @param from : 事件来源，{@link TabHostRewardSelectFrom}
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-03 09:59
     */
    @Override
    protected synchronized void changSelect(int old, int now, @TabHostRewardSelectFrom String from) {
        switch (from) {
            case TabHostRewardSelectFrom.FROM_HELPER:
            case TabHostRewardSelectFrom.FROM_TAB:
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(now);
                }
                break;
            case TabHostRewardSelectFrom.FROM_CONTENT:
                if (this.mCallback != null) {
                    if (old >= 0 && old < mList.size() && old != now) {
                        this.mCallback.unSelect(old, from);
                    }
                    this.mCallback.select(now, from);
                }
                break;
            default:
                break;
        }

        if (this.mSelect != now) {
            Fragment fragment;
            if (old >= 0 && old < mList.size() && old != now) {
                fragment = this.mList.get(old);
                if (fragment instanceof CloudUiLifeCallback) {
                    CloudUiLifeCallback uiLifeCallback = (CloudUiLifeCallback) fragment;
                    uiLifeCallback.onUiInVisible();
                }
            }
            fragment = this.mList.get(now);
            if (fragment instanceof CloudUiLifeCallback) {
                CloudUiLifeCallback uiLifeCallback = (CloudUiLifeCallback) fragment;
                uiLifeCallback.onUiVisible();
            }
        }

        this.mSelect = now;
    }

    /**
     * 设置容器
     *
     * @param viewGroup : ViewGroup，容器
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-02 11:14
     */
    @Override
    public void setViewGroup(ViewGroup viewGroup) {
        if (this.mContext == null) {
            Logger.Error("The context is error");
            return;
        }
        mViewPager = (ViewPager) viewGroup;
        mViewPager.addOnPageChangeListener(this);

        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < this.mList.size(); i++) {
            boolean isCanSelect = this.mCallback.isCanSelect(i);
            if (isCanSelect) {
                fragments.add(this.mList.get(i));
            }
        }

        PagerAdapter adapter;
//        if (this.mList.size() < MAX) {
        adapter = new ContentFragmentPagerAdapter(this.mFragmentManager, fragments);
//        } else {
//            adapter = new ContentFragmentStatePagerAdapter(this.mFragmentManager, this.mList);
//        }
        mAdapterSetting = (AdapterSettingListener) adapter;
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Logger.Info("onPageScrolled  position  :  " + position);
        Logger.Info("onPageScrolled  positionOffset  :  " + positionOffset);
        Logger.Info("onPageScrolled  positionOffsetPixels  :  " + positionOffsetPixels);
        if (mScrollState != SCROLL_IDLE) {
            return;
        }
        mScrollState = SCROLL_PROGRESS;

        Fragment fragment = mAdapterSetting.getFragment(position);
        if (fragment == null) {
            return;
        }
        int index = this.mList.indexOf(fragment);

        if (positionOffset < DIRECTION_MEDIAN) {
            //向左滑动
            index++;
        }
//        else {
//            //向右滑动
//            //do nothing
//        }
        if (index >= this.mList.size()) {
            return;
        }
        boolean isCanSelect = this.mCallback.isCanSelect(index);
        Logger.Info("index  :  " + index);
        Logger.Info("this.mList  start");
        for (Fragment fragment1 : mList) {
            Logger.Info("this.mList  :  " + fragment1.toString());
        }
        Logger.Info("this.mList  end");
        Logger.Info("  ");
        if (isCanSelect) {
            mAdapterSetting.addFragment(index, this.mList.get(index));
            return;
        }
        mAdapterSetting.removeFragment(index, this.mList.get(index));
    }

    @Override
    public synchronized void onPageSelected(int position) {
        Fragment fragment = mAdapterSetting.getFragment(position);
        if (fragment == null) {
            return;
        }
        int index = this.mList.indexOf(fragment);
        if (index != this.mSelect) {
            changSelect(this.mSelect, index, TabHostRewardSelectFrom.FROM_CONTENT);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                //未滑动
                mScrollState = SCROLL_IDLE;
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
                //开始滑动
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                //选择生效
                break;
            default:
                break;
        }
    }
}
