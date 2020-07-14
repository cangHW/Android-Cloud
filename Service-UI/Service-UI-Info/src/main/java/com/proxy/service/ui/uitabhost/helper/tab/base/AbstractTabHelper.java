package com.proxy.service.ui.uitabhost.helper.tab.base;

import android.content.Context;
import android.view.View;

import com.proxy.service.api.annotations.TabHostRewardSelectFrom;
import com.proxy.service.ui.uitabhost.TabHostHelper;
import com.proxy.service.ui.uitabhost.listener.TabCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/07/12  18:39
 */
public abstract class AbstractTabHelper implements ITabHelper {

    protected Context mContext;
    protected final List<View> mList = new ArrayList<>();
    protected TabCallback mCallback;
    protected int mSelect = TabHostHelper.SELECT_NULL;

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
    public void setData(List<View> list) {
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
    public void setCallback(TabCallback callback) {
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
    public void setSelect(int tabIndex, String from) {
        changSelect(mSelect, tabIndex, from, false);
    }

    /**
     * 切换选中
     *
     * @param old        : 当前选中
     * @param now        : 即将选中
     * @param from       : 事件来源，{@link TabHostRewardSelectFrom}
     * @param isCallback : 是否进行回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-03 09:59
     */
    protected abstract void changSelect(int old, int now, @TabHostRewardSelectFrom String from, boolean isCallback);
}
