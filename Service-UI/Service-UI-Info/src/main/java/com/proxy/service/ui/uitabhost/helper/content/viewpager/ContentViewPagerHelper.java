package com.proxy.service.ui.uitabhost.helper.content.viewpager;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.proxy.service.api.annotations.TabHostRewardSelectFrom;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.ui.uitabhost.helper.content.base.AbstractContentHelper;
import com.proxy.service.ui.uitabhost.helper.content.viewpager.adapter.ContentFragmentPagerAdapter;
import com.proxy.service.ui.uitabhost.helper.content.viewpager.adapter.ContentFragmentStatePagerAdapter;
import com.proxy.service.ui.uitabhost.helper.content.viewpager.cache.ViewCache;
import com.proxy.service.ui.uitabhost.helper.content.viewpager.fragment.PlaceHolderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/07/12  18:20
 */
public class ContentViewPagerHelper extends AbstractContentHelper implements ViewCache.ObtainViewListener, ViewPager.OnPageChangeListener {

    private static final int MAX = 5;
    private final String mTag = System.currentTimeMillis() + "_" + Math.random();
    private ViewPager mViewPager;

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
        this.mSelect = now;
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
        ViewCache.INSTANCE.addObtainViewListener(this);

        Bundle bundle = new Bundle();
        bundle.putString(PlaceHolderFragment.TAG, mTag);

        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < this.mList.size(); i++) {
            Object object = this.mList.get(i);

            if (object instanceof Fragment) {
                list.add((Fragment) object);
            } else if (object instanceof View) {
                Bundle args = (Bundle) bundle.clone();
                args.putInt(PlaceHolderFragment.INDEX, i);
                PlaceHolderFragment fragment = new PlaceHolderFragment();
                fragment.setArguments(args);
                list.add(fragment);
            } else {
                list.clear();
                this.mList.clear();
                Logger.Error(CloudApiError.UNKNOWN_ERROR.append("Discover unknown data. " + object.getClass().getCanonicalName()).build());
                return;
            }
        }

        PagerAdapter adapter;
        if (this.mList.size() < MAX) {
            adapter = new ContentFragmentPagerAdapter(this.mFragmentManager, list);
        } else {
            adapter = new ContentFragmentStatePagerAdapter(this.mFragmentManager, list);
        }
        mViewPager.setAdapter(adapter);
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
        if (index < 0) {
            return null;
        }

        if (index >= this.mList.size()) {
            return null;
        }

        Object object = this.mList.get(index);
        if (object instanceof View) {
            return (View) object;
        }

        return null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //TODO 判断是否可以选择
        //TODO viewpager 生命周期回调
        if (position != this.mSelect) {
            changSelect(this.mSelect, position, TabHostRewardSelectFrom.FROM_CONTENT);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
