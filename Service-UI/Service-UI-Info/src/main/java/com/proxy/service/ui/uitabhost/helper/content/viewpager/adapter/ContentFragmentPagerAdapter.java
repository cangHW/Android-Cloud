package com.proxy.service.ui.uitabhost.helper.content.viewpager.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.proxy.service.api.log.Logger;
import com.proxy.service.ui.uitabhost.helper.content.viewpager.listeners.AdapterSettingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/07/12  18:58
 */
public class ContentFragmentPagerAdapter extends FragmentPagerAdapter implements AdapterSettingListener {

    private final List<Fragment> mFragments = new ArrayList<>();

    public ContentFragmentPagerAdapter(@NonNull FragmentManager fm, @NonNull List<Fragment> list) {
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
     * 查询目标在数组中的位置
     *
     * @param fragment : 准备查询的目标
     * @return 获取到的位置
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/15 10:26 PM
     */
    @Override
    public int getIndex(Fragment fragment) {
        return mFragments.indexOf(fragment);
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
    public Fragment getFragment(int position) {
        if (position < 0 || position >= mFragments.size()) {
            return null;
        }
        return mFragments.get(position);
    }

    /**
     * 检查 fragment 数组，并按需刷新页面
     *
     * @param fragments : 发生改变后的 fragment 数组
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/14 6:52 PM
     */
    @Override
    public void changeFragment(@NonNull List<Fragment> fragments) {
        boolean flag = false;
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if (i >= mFragments.size()) {
                mFragments.add(fragment);
                flag = true;
                continue;
            }
            if (mFragments.get(i) != fragment) {
                mFragments.set(i, fragment);
                flag = true;
            }
        }
        if (flag) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public long getItemId(int position) {
        return mFragments.get(position).hashCode();
    }
}
