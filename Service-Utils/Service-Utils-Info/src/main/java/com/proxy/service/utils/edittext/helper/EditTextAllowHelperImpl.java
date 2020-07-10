package com.proxy.service.utils.edittext.helper;

import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.CloudTextChangedCallback;
import com.proxy.service.api.interfaces.IEditTextAllowHelper;
import com.proxy.service.utils.edittext.node.EditTextTypeInfo;

import java.util.List;

/**
 * @author: cangHX
 * on 2020/07/10  17:56
 */
public class EditTextAllowHelperImpl implements IEditTextAllowHelper {

    private List<CloudTextChangedCallback> mCallbacks;
    private StringBuilder mBuilder = new StringBuilder();

    public EditTextAllowHelperImpl(EditText editText, List<CloudTextChangedCallback> callbacks) {
        this.mCallbacks = callbacks;
        final int inputType = editText.getInputType();
        editText.setKeyListener(new NumberKeyListener() {
            @NonNull
            @Override
            protected char[] getAcceptedChars() {
                return mBuilder.toString().toCharArray();
            }

            @Override
            public int getInputType() {
                return inputType == InputType.TYPE_NULL ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : inputType;
            }
        });
    }

    /**
     * 设置允许输入数字
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    @Override
    public IEditTextAllowHelper allowNumber() {
        mBuilder.append(EditTextTypeInfo.NUMBER);
        return this;
    }

    /**
     * 设置允许输入字母
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    @Override
    public IEditTextAllowHelper allowLetter() {
        mBuilder.append(EditTextTypeInfo.LETTER);
        return this;
    }

    /**
     * 设置允许输入大写字母
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    @Override
    public IEditTextAllowHelper allowLetterUpperCase() {
        mBuilder.append(EditTextTypeInfo.LETTER_UPPERCASE);
        return this;
    }

    /**
     * 设置允许输入小写字母
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    @Override
    public IEditTextAllowHelper allowLetterLowerCase() {
        mBuilder.append(EditTextTypeInfo.LETTER_LOWERCASE);
        return this;
    }

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
    @Override
    public IEditTextAllowHelper allowDigits(@NonNull String digits) {
        mBuilder.append(digits);
        return this;
    }

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
    @Override
    public IEditTextAllowHelper addTextChangedCallback(@NonNull CloudTextChangedCallback callback) {
        if (this.mCallbacks != null) {
            this.mCallbacks.add(callback);
        }
        return this;
    }
}
