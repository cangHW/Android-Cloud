package com.proxy.service.ui.uitabhost.helper.content.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.proxy.service.ui.uitabhost.helper.base.IHelper;
import com.proxy.service.ui.uitabhost.listener.ContentCallback;

/**
 * 关联用户
 *
 * @author: cangHX
 * on 2020/07/02  14:15
 */
public interface IContentHelper extends IHelper<Fragment, ContentCallback> {

    /**
     * 设置 FragmentManager
     *
     * @param fragmentManager : 传入一个 FragmentManager 对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-02 11:13
     */
    void setFragmentManager(FragmentManager fragmentManager);

    /**
     * 刷新数据
     * 针对某些特殊情况，例如：viewpager 默认需要触发滑动才会刷新是否可以选中，
     * 在临界值时会出现第一次无法滑动选中，第二次可以滑动选中
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/16 1:22 PM
     */
    void refresh();
}
