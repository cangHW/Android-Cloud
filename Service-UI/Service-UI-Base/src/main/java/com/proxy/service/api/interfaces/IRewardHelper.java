package com.proxy.service.api.interfaces;

/**
 * @author: cangHX
 * on 2020/06/29  15:47
 */
public interface IRewardHelper {

    enum Set {
        /**
         * 标记当前事件发送给 UiEventCallback
         */
        UI_EVENT,

        /**
         * 设置选中页面
         */
        SELECT_INDEX
    }

    enum Get {
        /**
         * 获取上下文环境
         */
        CONTEXT,

        /**
         * 获取当前选中页面的index
         */
        SELECT_INDEX
    }

    /**
     * 根据标签获取数据
     *
     * @param helper : 标签
     * @return 获取的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-06 13:45
     */
    <T> T get(IRewardHelper.Get helper);

    /**
     * 根据标签进行事件发送
     *
     * @param helper : 标签
     * @param index  : 页面的 index，PS：如果是跳转事件{@link Set#SELECT_INDEX}，则 index 为目标页面的 index
     * @param tag    : 保留字段，开发者可以自定义使用
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-06 13:46
     */
    void set(IRewardHelper.Set helper, int index, Object tag);

}
