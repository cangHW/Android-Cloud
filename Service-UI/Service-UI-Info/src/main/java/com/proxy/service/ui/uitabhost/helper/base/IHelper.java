package com.proxy.service.ui.uitabhost.helper.base;

import android.content.Context;
import android.view.ViewGroup;

import com.proxy.service.api.annotations.TabHostRewardSelectFrom;

import java.util.List;

/**
 * 关联用户
 *
 * @author: cangHX
 * on 2020/07/02  10:54
 */
public interface IHelper<D, C> {

    /**
     * 设置上下文环境
     *
     * @param context : 上下文环境
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-02 14:53
     */
    void setContext(Context context);

    /**
     * 设置容器
     *
     * @param viewGroup : ViewGroup，容器
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-02 11:14
     */
    void setViewGroup(ViewGroup viewGroup);

    /**
     * 设置数据
     *
     * @param list : 数据数组
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-02 12:01
     */
    void setData(List<D> list);

    /**
     * 设置内容区域回调
     *
     * @param callback : 回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-02 11:59
     */
    void setCallback(C callback);

    /**
     * 设置选中的tab
     *
     * @param tabIndex : 用于标示tab
     * @param from     : 事件来源，{@link TabHostRewardSelectFrom}
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:19
     */
    void setSelect(int tabIndex, @TabHostRewardSelectFrom String from);

    /**
     * 根据class对象创建具体的对象
     *
     * @param tClass : 实现类的 class 对象
     * @return 具体的实现类对象
     * @throws Throwable 生成具体对象过程中的异常
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-02 11:00
     */
    static <T extends IHelper<?, ?>> T create(Class<T> tClass) throws Throwable {
        return tClass.getConstructor().newInstance();
    }

}
