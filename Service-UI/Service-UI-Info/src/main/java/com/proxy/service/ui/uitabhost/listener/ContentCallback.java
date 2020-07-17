package com.proxy.service.ui.uitabhost.listener;

/**
 * 内容区域回调
 *
 * @author: cangHX
 * on 2020/07/02  11:59
 */
public interface ContentCallback extends BaseUiTabHostCallback {

    /**
     * 选中进度变化
     *
     * @param index    : 目标下标
     * @param progress : 选中进度
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/16 3:41 PM
     */
    void onSelectProgress(int index, float progress);

    /**
     * 进度结束，可以在这个回调中对一些操作做还原处理
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/17 9:58 AM
     */
    void onSelectProgressEnd();
}
