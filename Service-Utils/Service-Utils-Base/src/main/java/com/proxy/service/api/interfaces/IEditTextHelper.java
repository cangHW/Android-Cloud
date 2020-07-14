package com.proxy.service.api.interfaces;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.CloudTextChangedCallback;

/**
 * @author: cangHX
 * on 2020/07/09  18:40
 */
public interface IEditTextHelper extends IEditTextBanHelper {

    /**
     * 清除当前输入框的输入格式，危险，
     * 如果随后没有设置允许输入格式，将导致无法输入
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    IEditTextAllowHelper clearInputType();

    /**
     * 设置文字改变监听
     *
     * @param callback : 文字改变监听回调
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 08:59
     */
    @Override
    @NonNull
    IEditTextHelper addTextChangedCallback(@NonNull CloudTextChangedCallback callback);

}
