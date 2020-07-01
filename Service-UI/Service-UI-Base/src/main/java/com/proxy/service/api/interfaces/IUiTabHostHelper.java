package com.proxy.service.api.interfaces;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.proxy.service.api.callback.CloudUiEventCallback;
import com.proxy.service.base.BaseService;

/**
 * @author: cangHX
 * on 2020/07/01  09:29
 */
public interface IUiTabHostHelper<T> extends BaseService {

    /**
     * 设置依赖的 Activity
     * <p>
     * 两个方法执行一个即可，{@link com.proxy.service.api.interfaces.IUiTabHostHelper#setFragment(Fragment)}
     *
     * @param fragmentActivity : 依赖的 Activity
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-30 18:22
     */
    @NonNull
    T setActivity(FragmentActivity fragmentActivity);

    /**
     * 设置依赖的 Fragment
     * <p>
     * 两个方法执行一个即可，{@link com.proxy.service.api.interfaces.IUiTabHostHelper#setActivity(FragmentActivity)}
     *
     * @param fragment : 依赖的 Fragment
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-30 18:24
     */
    @NonNull
    T setFragment(Fragment fragment);

    /**
     * 设置内容区域
     * 如果是 viewpager 或者 viewpager2 建议实现对应接口，方便获取更加准确的生命周期
     * 接口请看{@link com.proxy.service.api.callback.CloudUiLifeCallback}
     *
     * @param viewGroup : ViewGroup，用于展示内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 09:33
     */
    @NonNull
    T setContentSpace(ViewGroup viewGroup);

    /**
     * 设置tab区域
     *
     * @param viewGroup : ViewGroup，用于展示tab
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 09:35
     */
    @NonNull
    T setTabSpace(ViewGroup viewGroup);

    /**
     * 添加事件回调
     *
     * @param cloudUiEventCallback : 用于接收事件回调的监听
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:10
     */
    @NonNull
    T addEventCallback(CloudUiEventCallback cloudUiEventCallback);

    /**
     * 设置选中的tab
     *
     * @param tabIndex : 用于标示tab
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:19
     */
    @NonNull
    T setSelect(int tabIndex);

}
