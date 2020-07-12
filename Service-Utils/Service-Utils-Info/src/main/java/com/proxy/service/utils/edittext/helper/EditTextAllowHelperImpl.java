package com.proxy.service.utils.edittext.helper;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.CloudTextChangedCallback;
import com.proxy.service.api.interfaces.IEditTextAllowHelper;
import com.proxy.service.utils.edittext.node.EditTextTypeInfo;
import com.proxy.service.utils.util.StringUtils;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author: cangHX
 * on 2020/07/10  17:56
 */
public class EditTextAllowHelperImpl implements IEditTextAllowHelper, InputFilter {

    private EditTextTypeInfo mInfo = new EditTextTypeInfo();
    private Set<CloudTextChangedCallback> mCallbacks;
    private Pattern mDigitsPattern;
    private Pattern mRegexPattern;

    public EditTextAllowHelperImpl(EditText editText, Set<CloudTextChangedCallback> callbacks) {
        this.mCallbacks = callbacks;

        mInfo.setNumberEnable(false);
        mInfo.setLetterEnable(false);
        mInfo.setLetterUpperCaseEnable(false);
        mInfo.setLetterLowerCaseEnable(false);
        mInfo.setEmojiEnable(false);

        editText.setFilters(new InputFilter[]{
                EditTextAllowHelperImpl.this
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
        mInfo.setNumberEnable(true);
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
        mInfo.setLetterEnable(true);
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
        mInfo.setLetterUpperCaseEnable(true);
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
        mInfo.setLetterLowerCaseEnable(true);
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
        if (TextUtils.isEmpty(digits)) {
            return this;
        }
        mDigitsPattern = Pattern.compile("[" + digits + "]");
        return this;
    }

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
    @Override
    public IEditTextAllowHelper allowMatcher(@NonNull String regex) {
        if (TextUtils.isEmpty(regex)) {
            return this;
        }
        mRegexPattern = Pattern.compile(regex);
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

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (mInfo.isEmojiEnable() && StringUtils.checkEmoji(source)) {
            return null;
        }
        if (mInfo.isNumberEnable() && StringUtils.checkNumber(source)) {
            return null;
        }
        if (mInfo.isLetterEnable() && StringUtils.checkLetter(source)) {
            return null;
        }
        if (mInfo.isLetterLowerCaseEnable() && StringUtils.checkLetterLowerCase(source)) {
            return null;
        }
        if (mInfo.isLetterUpperCaseEnable() && StringUtils.checkLetterUpperCase(source)) {
            return null;
        }
        if (mDigitsPattern != null && StringUtils.checkMatcher(mDigitsPattern, source)) {
            return null;
        }
        if (mRegexPattern != null && StringUtils.checkMatcher(mRegexPattern, source)) {
            return null;
        }
        return "";
    }
}
