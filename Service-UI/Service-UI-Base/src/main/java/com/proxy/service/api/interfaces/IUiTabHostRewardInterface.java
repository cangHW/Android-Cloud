package com.proxy.service.api.interfaces;

import androidx.annotation.NonNull;

import com.proxy.service.api.annotations.TabHostRewardSelectFrom;

/**
 * @author: cangHX
 * on 2020/06/29  15:46
 * <p>
 * android 选项卡类型页面顶级接口
 */
public interface IUiTabHostRewardInterface<C, T> {

    /**
     * 设置工具类，可以实现通信、跳转、获取部分数据等功能
     *
     * @param iRewardHelper : 工具类对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 15:48
     */
    void setRewardHelper(@NonNull IRewardHelper iRewardHelper);

    /**
     * 获取显示内容
     *
     * @return 显示内容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 15:52
     */
    @NonNull
    C getContent();

    /**
     * 获取显示tab
     *
     * @return 显示tab
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 15:52
     */
    @NonNull
    T getTab();

    /**
     * 获取展示位置
     *
     * @return 展示位置
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 15:53
     */
    int getIndex();

    /**
     * 是否可以选中
     *
     * @return true 可以选中，false 不允许选中
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 15:58
     */
    boolean isCanSelect();

    /**
     * 是否默认选中
     *
     * @return true 选中，false 不选中
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 15:59
     */
    boolean isDefaultSelect();

    /**
     * 状态选中
     *
     * @param from : 选中状态来源
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 16:02
     */
    void onSelect(@TabHostRewardSelectFrom String from);

    /**
     * 状态未选中
     *
     * @param from : 选中状态来源
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 16:05
     */
    void onUnSelect(@TabHostRewardSelectFrom String from);

}
