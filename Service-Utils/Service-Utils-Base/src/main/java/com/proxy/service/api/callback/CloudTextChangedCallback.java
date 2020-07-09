package com.proxy.service.api.callback;

import android.view.View;

/**
 * 文字变化回调接口
 *
 * @author: cangHX
 * on 2020/07/09  18:26
 */
public interface CloudTextChangedCallback {

    /**
     * 文字变化
     *
     * @param view    : 发生变化的 view
     * @param value   : 发生改变后的内容
     * @param isEmpty : 是否为空
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-09 18:27
     */
    void onChanged(View view, String value, boolean isEmpty);

}
