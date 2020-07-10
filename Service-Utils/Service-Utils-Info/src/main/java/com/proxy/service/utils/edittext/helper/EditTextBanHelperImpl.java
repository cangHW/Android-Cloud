package com.proxy.service.utils.edittext.helper;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.CloudTextChangedCallback;
import com.proxy.service.api.interfaces.IEditTextBanHelper;
import com.proxy.service.utils.edittext.node.EditTextTypeInfo;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: cangHX
 * on 2020/07/10  17:55
 */
public class EditTextBanHelperImpl implements IEditTextBanHelper, InputFilter {

    private EditTextTypeInfo mInfo = new EditTextTypeInfo();
    private List<CloudTextChangedCallback> mCallbacks;
    private Pattern mDigitsPattern;

    public EditTextBanHelperImpl(EditText editText, List<CloudTextChangedCallback> callbacks) {
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
        mInfo.banEmoji();
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
        mInfo.banNumber();
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
        mInfo.banLetter();
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
        mInfo.banLetterLowerCase();
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
        mInfo.banLetterUpperCase();
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
        mDigitsPattern = Pattern.compile(digits);
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
        if (mInfo.isEmojiEnable() && checkEmoji(source)) {
            return "";
        }
        if (mInfo.isNumberEnable() && checkNumber(source)) {
            return "";
        }
        if (mInfo.isLetterEnable() && checkLetter(source)) {
            return "";
        }
        if (mInfo.isLetterLowerCaseEnable() && checkLetterLowerCase(source)) {
            return "";
        }
        if (mInfo.isLetterUpperCaseEnable() && checkLetterUpperCase(source)) {
            return "";
        }
        if (mDigitsPattern != null && checkDigits(source)) {
            return "";
        }
        return null;
    }

    /**
     * 自定义数据检测
     *
     * @param source : 待检测对象
     * @return true 包含，false 不包含
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 18:49
     */
    private boolean checkDigits(CharSequence source) {
        Matcher matcher = mDigitsPattern.matcher(source);
        return matcher.find();
    }

    /**
     * 检测是否包含大写英文
     *
     * @param source : 待检测对象
     * @return true 包含，false 不包含
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 18:49
     */
    private boolean checkLetterUpperCase(CharSequence source) {
        Matcher matcher = mInfo.mLetterUpperCasePattern.matcher(source);
        return matcher.find();
    }

    /**
     * 检测是否包含小写英文
     *
     * @param source : 待检测对象
     * @return true 包含，false 不包含
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 18:49
     */
    private boolean checkLetterLowerCase(CharSequence source) {
        Matcher matcher = mInfo.mLetterLowerCasePattern.matcher(source);
        return matcher.find();
    }

    /**
     * 检测是否包含英文
     *
     * @param source : 待检测对象
     * @return true 包含，false 不包含
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 18:49
     */
    private boolean checkLetter(CharSequence source) {
        Matcher matcher = mInfo.mLetterPattern.matcher(source);
        return matcher.find();
    }

    /**
     * 检测是否包含数字
     *
     * @param source : 待检测对象
     * @return true 包含，false 不包含
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 18:49
     */
    private boolean checkNumber(CharSequence source) {
        Matcher matcher = mInfo.mNumberPattern.matcher(source);
        return matcher.find();
    }

    /**
     * 检测是否包含表情
     *
     * @param source : 待检测对象
     * @return true 包含，false 不包含
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 18:46
     */
    private boolean checkEmoji(CharSequence source) {
        //第一重过滤,这个方法能过滤掉大部分表情,但有极个别的过滤不掉,故加上下面的方法进行双重过滤
        Matcher matcher = mInfo.mEmojiPattern.matcher(source);
        if (matcher.find()) {
            return true;
        }
        //第二重过滤,增强排查力度,如果还有漏网之鱼，则进行第三次过滤(暂不考虑第三次过滤)
        return containsEmoji(source.toString());
    }

    /**
     * 检测 string 字符串中是否包含表情
     *
     * @param source : 待检测的字符串
     * @return true 包含，false 不包含
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 14:09
     */
    private static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测字符是否为表情
     *
     * @param codePoint : 待检测的字符
     * @return true 是表情，false 不是表情
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 14:10
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return codePoint == 0x0 || codePoint == 0x9 || codePoint == 0xA || codePoint == 0xD || codePoint >= 0x20 && codePoint <= 0xD7FF || codePoint >= 0xE000 && codePoint <= 0xFFFD;
    }
}
