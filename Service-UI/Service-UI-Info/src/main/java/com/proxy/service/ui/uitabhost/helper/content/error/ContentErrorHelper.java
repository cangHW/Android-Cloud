package com.proxy.service.ui.uitabhost.helper.content.error;

import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.proxy.service.ui.uitabhost.helper.content.base.IContentHelper;
import com.proxy.service.ui.uitabhost.listener.ContentCallback;

import java.util.List;

/**
 * @author: cangHX
 * on 2020/07/02  14:24
 */
public class ContentErrorHelper implements IContentHelper {
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
     * @param from     : 事件来源，{@link TabHostRewardSelectFrom}
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:19
     */
    @Override
    public void setSelect(int tabIndex, String from) {

    }
}
