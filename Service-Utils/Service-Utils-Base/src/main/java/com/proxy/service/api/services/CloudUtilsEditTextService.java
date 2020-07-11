package com.proxy.service.api.services;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.interfaces.IEditTextHelper;
import com.proxy.service.base.BaseService;

/**
 * EditText控件管理类
 *
 * @author: cangHX
 * on 2020/07/09  18:24
 */
public interface CloudUtilsEditTextService extends BaseService {

    /**
     * 设置输入框属性
     *
     * @param editText : EditText
     * @return 辅助类
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-09 18:40
     */
    @NonNull
    IEditTextHelper with(@NonNull EditText editText);

    /**
     * 弹出键盘
     *
     * @param editText : EditText
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-09 18:37
     */
    void showSoftInput(@NonNull EditText editText);

    /**
     * 隐藏键盘
     *
     * @param view : 任意 view
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-09 18:38
     */
    void hideSoftInput(@Nullable View view);

    /**
     * 隐藏输入内容，用点号替换文字
     *
     * @param editText : EditText
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-11 10:36
     */
    void hideInputContent(@NonNull EditText editText);

    /**
     * 显示输入内容
     *
     * @param editText : EditText
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-11 10:36
     */
    void showInputContent(@NonNull EditText editText);
}
