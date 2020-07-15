package com.proxy.service.ui.uitabhost.helper.content.viewpager.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.proxy.service.ui.uitabhost.helper.content.viewpager.listeners.AdapterSettingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/07/12  18:59
 */
public class ContentFragmentStatePagerAdapter extends FragmentStatePagerAdapter implements AdapterSettingListener {

    private final List<Fragment> mFragments = new ArrayList<>();

    public ContentFragmentStatePagerAdapter(@NonNull FragmentManager fm, @NonNull List<Fragment> list) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFragments.clear();
        mFragments.addAll(list);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    /**
     * 获取 fragment
     *
     * @param position : 目标位置
     * @return 获取到的 fragment
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/14 6:51 PM
     */
    @Override
    public synchronized Fragment getFragment(int position) {
        if (position < 0 || position >= mFragments.size()) {
            return null;
        }
        return mFragments.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    /**
     * 添加 fragment 并刷新页面
     *
     * @param index    : 目标位置
     * @param fragment : 准备添加的 fragment
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/14 6:52 PM
     */
    @Override
    public void addFragment(int index, @NonNull Fragment fragment) {
        Fragment nowFragment = this.mFragments.get(index);
        if (nowFragment != fragment) {
            this.mFragments.add(index, fragment);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 移除 fragment 并刷新页面
     *
     * @param index    : 目标位置
     * @param fragment : 准备移除的 fragment
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/14 6:52 PM
     */
    @Override
    public void removeFragment(int index, @NonNull Fragment fragment) {
        Fragment nowFragment = this.mFragments.get(index);
        if (nowFragment == fragment) {
            this.mFragments.remove(index);
            this.notifyDataSetChanged();
        }
    }
}
