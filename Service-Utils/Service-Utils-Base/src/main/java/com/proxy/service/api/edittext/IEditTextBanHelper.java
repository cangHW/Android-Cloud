package com.proxy.service.api.edittext;

import androidx.annotation.NonNull;

/**
 * 设置允许输入
 *
 * @author: cangHX
 * on 2020/07/10  17:39
 */
public interface IEditTextBanHelper {

    /**
     * 禁止输入表情
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    IEditTextBanHelper banEmoji();

    /**
     * 禁止输入数字
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    IEditTextBanHelper banNumber();

    /**
     * 禁止输入英文
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    IEditTextBanHelper banLetter();

    /**
     * 禁止输入小写英文
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    IEditTextBanHelper banLetterLowerCase();

    /**
     * 禁止输入大写英文
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    IEditTextBanHelper banLetterUpperCase();

    /**
     * 自定义禁止输入的内容
     *
     * @param digits : 自定义内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    IEditTextBanHelper banDigits(@NonNull String digits);

    /**
     * 自定义禁止输入正则
     *
     * @param regex : 正则表达式
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-12 11:56
     */
    @NonNull
    IEditTextBanHelper banMatcher(@NonNull String regex);

    /**
     * 设置文字改变监听
     *
     * @param callback : 文字改变监听回调
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 08:59
     */
    @NonNull
    IEditTextBanHelper addTextChangedCallback(@NonNull CloudTextChangedCallback callback);
}
