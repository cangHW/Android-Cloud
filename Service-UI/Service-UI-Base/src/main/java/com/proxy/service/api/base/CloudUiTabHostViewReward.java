package com.proxy.service.api.base;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.proxy.service.api.annotations.TabHostRewardSelectFrom;
import com.proxy.service.api.interfaces.IRewardHelper;
import com.proxy.service.api.interfaces.IUiTabHostRewardInterface;

/**
 * 子类需添加注解
 * {@link com.proxy.service.annotations.CloudUiTabHostReward}
 *
 * @author: cangHX
 * on 2020/06/29  15:42
 */
@SuppressWarnings("WeakerAccess")
public abstract class CloudUiTabHostViewReward implements IUiTabHostRewardInterface<View> {

    /**
     * 工具类对象，拥有多项功能
     */
    protected IRewardHelper mRewardHelper;

    /**
     * 上下文环境
     */
    protected Context mContext;

    /**
     * content 对象
     */
    protected View mContentView;

    /**
     * tab 对象
     */
    protected View mTabView;

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
        this.mRewardHelper = iRewardHelper;
        mContext = iRewardHelper.get(IRewardHelper.Get.CONTEXT);
    }

    /**
     * 获取显示内容
     *
     * @return 显示内容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 15:52
     */
    @NonNull
    @Override
    public View getContent() {
        mContentView = getContent(mContext);
        return mContentView;
    }

    /**
     * 获取显示tab
     *
     * @return 显示tab
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 15:52
     */
    @NonNull
    @Override
    public View getTab() {
        mTabView = getTab(mContext);
        return mTabView;
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
     * 选中进度变化
     *
     * @param progress : 选中进度
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/16 3:41 PM
     */
    @Override
    public void onSelectProgress(float progress) {

    }

    /**
     * 进度结束，可以在这个回调中对一些操作做还原处理
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/17 9:58 AM
     */
    @Override
    public void onSelectProgressEnd() {

    }

    /**
     * 状态选中
     *
     * @param from : 选中状态来源，{@link TabHostRewardSelectFrom}
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 16:02
     */
    @Override
    public void onSelect(@TabHostRewardSelectFrom String from) {

    }

    /**
     * 状态未选中
     *
     * @param from : 选中状态来源，{@link TabHostRewardSelectFrom}
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 16:05
     */
    @Override
    public void onUnSelect(@TabHostRewardSelectFrom String from) {

    }

    /**
     * 获取显示内容
     *
     * @param context : 上下文环境
     * @return 显示内容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 15:52
     */
    @NonNull
    public abstract View getContent(Context context);

    /**
     * 获取显示tab
     *
     * @param context : 上下文环境
     * @return 显示tab
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 15:52
     */
    @NonNull
    public abstract View getTab(Context context);
}
