package com.proxy.service.api.interfaces;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.CloudTextChangedCallback;

/**
 * 设置允许输入
 *
 * @author: cangHX
 * on 2020/07/10  17:39
 */
public interface IEditTextAllowHelper {

    /**
     * 设置允许输入数字
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    IEditTextAllowHelper allowNumber();

    /**
     * 设置允许输入字母
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    IEditTextAllowHelper allowLetter();

    /**
     * 设置允许输入大写字母
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    IEditTextAllowHelper allowLetterUpperCase();

    /**
     * 设置允许输入小写字母
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    IEditTextAllowHelper allowLetterLowerCase();

    /**
     * 自定义允许输入的内容
     *
     * @param digits : 自定义内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    IEditTextAllowHelper allowDigits(@NonNull String digits);

    /**
     * 自定义允许输入正则
     *
     * @param regex : 正则表达式
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-12 11:56
     */
    @NonNull
    IEditTextAllowHelper allowMatcher(@NonNull String regex);

    /**
     * 设置文字改变监听
     *
     * @param callback : 文字改变监听回调集合
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 08:59
     */
    @NonNull
    IEditTextAllowHelper addTextChangedCallback(@NonNull CloudTextChangedCallback callback);
}
