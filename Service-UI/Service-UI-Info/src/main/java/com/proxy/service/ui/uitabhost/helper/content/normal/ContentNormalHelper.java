package com.proxy.service.ui.uitabhost.helper.content.normal;

import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.proxy.service.api.annotations.TabHostRewardSelectFrom;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.ui.uitabhost.helper.content.base.AbstractContentHelper;
import com.proxy.service.ui.util.ViewUtils;

/**
 * @author: cangHX
 * on 2020/07/02  14:23
 */
public class ContentNormalHelper extends AbstractContentHelper {

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

        for (Object object : mList) {
            if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.add(viewGroup.getId(), fragment);
                fragmentTransaction.hide(fragment);
                fragmentTransaction.commitAllowingStateLoss();
            } else if (object instanceof View) {
                View view = (View) object;
                view.setVisibility(View.GONE);
                ViewGroup.LayoutParams params = ViewUtils.getLayoutParams(viewGroup, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                viewGroup.addView(view, params);
            }
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
        if (old == now) {
            return;
        }

        if (old >= 0 && old < mList.size()) {
            Object oldObject = mList.get(old);
            if (oldObject instanceof Fragment) {
                Fragment fragment = (Fragment) oldObject;
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.hide(fragment);
                fragmentTransaction.commitAllowingStateLoss();
            } else if (oldObject instanceof View) {
                View view = (View) oldObject;
                view.setVisibility(View.GONE);
            }
        }

        Object nowObject = mList.get(now);
        if (nowObject instanceof Fragment) {
            Fragment fragment = (Fragment) nowObject;
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.show(fragment);
            fragmentTransaction.commitAllowingStateLoss();
        } else if (nowObject instanceof View) {
            View view = (View) nowObject;
            view.setVisibility(View.VISIBLE);
        }

        this.mSelect = now;
    }
}
