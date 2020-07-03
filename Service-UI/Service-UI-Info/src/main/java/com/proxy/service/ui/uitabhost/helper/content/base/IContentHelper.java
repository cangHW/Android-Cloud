package com.proxy.service.ui.uitabhost.helper.content.base;

import androidx.fragment.app.FragmentManager;

import com.proxy.service.ui.uitabhost.helper.base.IHelper;
import com.proxy.service.ui.uitabhost.listener.ContentCallback;

/**
 * @author: cangHX
 * on 2020/07/02  14:15
 * <p>
 * 关联用户
 */
public interface IContentHelper extends IHelper<Object, ContentCallback> {

    /**
     * 设置 FragmentManager
     *
     * @param fragmentManager : 传入一个 FragmentManager 对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-02 11:13
     */
    void setFragmentManager(FragmentManager fragmentManager);


}
