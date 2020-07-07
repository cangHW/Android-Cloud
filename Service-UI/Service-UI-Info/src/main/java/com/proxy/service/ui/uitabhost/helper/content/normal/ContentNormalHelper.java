package com.proxy.service.ui.uitabhost.helper.content.normal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.proxy.service.api.utils.Logger;
import com.proxy.service.ui.uitabhost.TabHostHelper;
import com.proxy.service.ui.uitabhost.helper.content.base.IContentHelper;
import com.proxy.service.ui.uitabhost.listener.ContentCallback;
import com.proxy.service.ui.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/07/02  14:23
 */
public class ContentNormalHelper implements IContentHelper {

    private Context mContext;
    private List<Object> mList = new ArrayList<>();
    private FragmentManager mFragmentManager;
    private int mSelect = TabHostHelper.SELECT_NULL;

    /**
     * 设置 FragmentManager
     *
     * @param fragmentManager : 传入一个 FragmentManager 对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-02 11:13
     */
    @Override
    public void setFragmentManager(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    /**
     * 设置上下文环境
     *
     * @param context : 上下文环境
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-02 14:53
     */
    @Override
    public void setContext(Context context) {
        this.mContext = context;
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
     * 设置数据
     *
     * @param list : 数据数组
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-02 12:01
     */
    @Override
    public void setData(List<Object> list) {
        if (list == null) {
            return;
        }
        this.mList.clear();
        this.mList.addAll(list);
    }

    /**
     * 设置内容区域回调
     *
     * @param callback : 回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-02 11:59
     */
    @Override
    public void setCallback(ContentCallback callback) {
    }

    /**
     * 设置选中的tab
     *
     * @param tabIndex : 用于标示tab
     * @param from     : 事件来源
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:19
     */
    @Override
    public void setSelect(int tabIndex, String from) {
        changSelect(mSelect, tabIndex);
    }

    /**
     * 切换选中
     *
     * @param old : 当前选中
     * @param now : 即将选中
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-03 09:59
     */
    private synchronized void changSelect(int old, int now) {
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
