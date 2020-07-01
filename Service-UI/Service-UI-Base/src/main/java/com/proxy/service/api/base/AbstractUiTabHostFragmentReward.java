package com.proxy.service.api.base;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.proxy.service.api.annotations.TabHostRewardSelectFrom;
import com.proxy.service.api.interfaces.IRewardHelper;
import com.proxy.service.api.interfaces.IUiTabHostRewardInterface;

/**
 * @author: cangHX
 * on 2020/06/29  15:42
 * <p>
 * 子类需添加注解
 * {@link com.proxy.service.annotations.CloudUiTabHostReward}
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractUiTabHostFragmentReward implements IUiTabHostRewardInterface<Fragment> {
    /**
     * 设置工具类，可以实现通信、跳转、获取部分数据等功能
     *
     * @param iRewardHelper : 工具类对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 15:48
     */
    @Override
    public void setRewardHelper(@NonNull IRewardHelper iRewardHelper) {

    }

    /**
     * 是否可以选中
     *
     * @return true 可以选中，false 不允许选中
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 15:58
     */
    @Override
    public boolean isCanSelect() {
        return true;
    }

    /**
     * 是否默认选中
     *
     * @return true 选中，false 不选中
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 15:59
     */
    @Override
    public boolean isDefaultSelect() {
        return false;
    }

    /**
     * 状态选中
     *
     * @param from : 选中状态来源
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 16:02
     */
    @Override
    public void onSelect(@TabHostRewardSelectFrom String from) {
        onSelect();
    }

    /**
     * 状态未选中
     *
     * @param from : 选中状态来源
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 16:05
     */
    @Override
    public void onUnSelect(@TabHostRewardSelectFrom String from) {
        onUnSelect();
    }

    /**
     * 状态选中
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 16:02
     */
    public abstract void onSelect();

    /**
     * 状态未选中
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 16:05
     */
    public abstract void onUnSelect();
}
