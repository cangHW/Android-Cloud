package com.proxy.service.utils.edittext.helper;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.proxy.service.api.edittext.CloudTextChangedCallback;
import com.proxy.service.api.edittext.IEditTextBanHelper;
import com.proxy.service.utils.edittext.node.EditTextTypeInfo;
import com.proxy.service.utils.util.StringUtils;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author: cangHX
 * on 2020/07/10  17:55
 */
public class EditTextBanHelperImpl implements IEditTextBanHelper, InputFilter {

    private final EditTextTypeInfo mInfo = new EditTextTypeInfo();
    private final Set<CloudTextChangedCallback> mCallbacks;
    private Pattern mDigitsPattern;
    private Pattern mRegexPattern;

    public EditTextBanHelperImpl(EditText editText, Set<CloudTextChangedCallback> callbacks) {
        this.mCallbacks = callbacks;
        editText.setFilters(new InputFilter[]{
                EditTextBanHelperImpl.this
        });
    }

    /**
     * 禁止输入表情
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    @Override
    public IEditTextBanHelper banEmoji() {
        mInfo.setEmojiEnable(false);
        return this;
    }

    /**
     * 禁止输入数字
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    @Override
    public IEditTextBanHelper banNumber() {
        mInfo.setNumberEnable(false);
        return this;
    }

    /**
     * 禁止输入英文
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    @Override
    public IEditTextBanHelper banLetter() {
        mInfo.setLetterEnable(false);
        return this;
    }

    /**
     * 禁止输入小写英文
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    @Override
    public IEditTextBanHelper banLetterLowerCase() {
        mInfo.setLetterLowerCaseEnable(false);
        return this;
    }

    /**
     * 禁止输入大写英文
     *
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 09:04
     */
    @NonNull
    @Override
    public IEditTextBanHelper banLetterUpperCase() {
        mInfo.setLetterUpperCaseEnable(false);
        return this;
    }

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
    @Override
    public IEditTextBanHelper banDigits(@NonNull String digits) {
        if (TextUtils.isEmpty(digits)) {
            return this;
        }
        mDigitsPattern = Pattern.compile("[" + digits + "]");
        return this;
    }

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
    @Override
    public IEditTextBanHelper banMatcher(@NonNull String regex) {
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
    public IEditTextBanHelper addTextChangedCallback(@NonNull CloudTextChangedCallback callback) {
        if (this.mCallbacks != null) {
            this.mCallbacks.add(callback);
        }
        return this;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (!mInfo.isEmojiEnable() && StringUtils.checkEmoji(source)) {
            return "";
        }
        if (!mInfo.isNumberEnable() && StringUtils.checkNumber(source)) {
            return "";
        }
        if (!mInfo.isLetterEnable() && StringUtils.checkLetter(source)) {
            return "";
        }
        if (!mInfo.isLetterLowerCaseEnable() && StringUtils.checkLetterLowerCase(source)) {
            return "";
        }
        if (!mInfo.isLetterUpperCaseEnable() && StringUtils.checkLetterUpperCase(source)) {
            return "";
        }
        if (mDigitsPattern != null && StringUtils.checkMatcher(mDigitsPattern, source)) {
            return "";
        }
        if (mRegexPattern != null && StringUtils.checkMatcher(mRegexPattern, source)) {
            return "";
        }
        return null;
    }

}
