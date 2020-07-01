package com.proxy.service.ui.uitabhost.normal;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.proxy.service.api.callback.CloudUiLifeCallback;
import com.proxy.service.ui.uitabhost.base.AbstractTabHostHelper;

/**
 * @author: cangHX
 * on 2020/06/30  18:51
 */
public class NormalTabHostHelperImpl extends AbstractTabHostHelper {

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
    @Override
    public AbstractTabHostHelper setActivity(FragmentActivity fragmentActivity) {
        return this;
    }

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
    @Override
    public AbstractTabHostHelper setFragment(Fragment fragment) {
        return this;
    }

    /**
     * 设置内容区域
     * 如果是 viewpager 或者 viewpager2 建议实现对应接口，方便获取更加准确的生命周期
     * 接口请看{@link CloudUiLifeCallback}
     *
     * @param viewGroup : ViewGroup，用于展示内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 09:33
     */
    @NonNull
    @Override
    public AbstractTabHostHelper setContentSpace(ViewGroup viewGroup) {
        return this;
    }

    /**
     * 修改选中
     *
     * @param now : 当前选中
     * @param old : 之前选中
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-01 10:25
     */
    @Override
    protected void changeSelect(int now, int old) {

        this.mSelectIndex = now;
    }
}
