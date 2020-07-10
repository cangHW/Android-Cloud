package com.proxy.service.api.callback;

import android.text.Editable;
import android.view.View;

import androidx.annotation.NonNull;

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
     * @param view     : 发生变化的 view
     * @param newValue : 改变后的内容
     * @param oldValue : 改变前的内容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-09 18:27
     */
    void onChanged(@NonNull View view, @NonNull Editable newValue, @NonNull String oldValue);

}
