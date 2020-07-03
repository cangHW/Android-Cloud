package com.proxy.service.ui.uitabhost.listener;

import com.proxy.service.api.annotations.TabHostRewardSelectFrom;

/**
 * @author: cangHX
 * on 2020/07/03  17:33
 */
public interface BaseUiTabHostCallback {

    /**
     * 选中
     *
     * @param index : 选中的 index
     * @param from  : 事件来源
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-03 10:44
     */
    void select(int index, @TabHostRewardSelectFrom String from);

    /**
     * 取消选中
     *
     * @param index : 选中的 index
     * @param from  : 事件来源
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-03 17:31
     */
    void unSelect(int index, @TabHostRewardSelectFrom String from);

}
