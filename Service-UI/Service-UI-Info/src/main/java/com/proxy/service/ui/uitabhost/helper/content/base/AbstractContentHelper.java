package com.proxy.service.ui.uitabhost.helper.content.base;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.proxy.service.api.annotations.TabHostRewardSelectFrom;
import com.proxy.service.ui.uitabhost.TabHostHelper;
import com.proxy.service.ui.uitabhost.listener.ContentCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/07/12  18:32
 */
public abstract class AbstractContentHelper implements IContentHelper {

    protected Context mContext;
    protected final List<Fragment> mList = new ArrayList<>();
    protected FragmentManager mFragmentManager;
    protected ContentCallback mCallback;
    protected int mSelect = TabHostHelper.SELECT_NULL;

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
     * 设置数据
     *
     * @param list : 数据数组
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-02 12:01
     */
    @Override
    public void setData(List<Fragment> list) {
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
        this.mCallback = callback;
    }

    /**
     * 设置选中的tab
     *
     * @param tabIndex : 用于标示tab
     * @param from     : 事件来源，{@link TabHostRewardSelectFrom}
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:19
     */
    @Override
    public synchronized void setSelect(int tabIndex, @TabHostRewardSelectFrom String from) {
        changSelect(mSelect, tabIndex, from);
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
    protected abstract void changSelect(int old, int now, @TabHostRewardSelectFrom String from);
}
