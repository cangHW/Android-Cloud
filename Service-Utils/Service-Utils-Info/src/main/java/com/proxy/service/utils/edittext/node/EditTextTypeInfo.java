package com.proxy.service.utils.edittext.node;

import java.util.regex.Pattern;

/**
 * @author: cangHX
 * on 2020/07/10  09:57
 */
public class EditTextTypeInfo {

    private static final String INPUT_SYMBOL = "`~!@#$%^&*()_+-=[]{}|;':,./\\，。、；’【】、";

    public static final String LETTER_LOWERCASE = "[abcdefghijklmnopqrstuvwxyz]";
    public Pattern mLetterLowerCasePattern = Pattern.compile(LETTER_LOWERCASE);

    public static final String LETTER_UPPERCASE = "[ABCDEFGHIJKLMNOPQRSTUVWXYZ]";
    public Pattern mLetterUpperCasePattern = Pattern.compile(LETTER_UPPERCASE);

    public static final String LETTER = "[" + LETTER_LOWERCASE + LETTER_UPPERCASE + "]";
    public Pattern mLetterPattern = Pattern.compile(LETTER);

    public static final String NUMBER = "[0123456789]";
    public Pattern mNumberPattern = Pattern.compile(NUMBER);

    private static final String EMOJI = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
    public Pattern mEmojiPattern = Pattern.compile(EMOJI, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    private boolean isNumberEnable = true;

    private boolean isLetterEnable = true;

    private boolean isLetterUpperCaseEnable = true;

    private boolean isLetterLowerCaseEnable = true;

    private boolean isEmojiEnable = true;

    /**
     * 数字是否可用
     */
    public boolean isNumberEnable() {
        return isNumberEnable;
    }

    /**
     * 字母是否可用
     */
    public boolean isLetterEnable() {
        return isLetterEnable;
    }

    /**
     * 大写字母是否可用
     */
    public boolean isLetterUpperCaseEnable() {
        return isLetterUpperCaseEnable;
    }

    /**
     * 小写字母是否可用
     */
    public boolean isLetterLowerCaseEnable() {
        return isLetterLowerCaseEnable;
    }

    /**
     * 表情是否可用
     */
    public boolean isEmojiEnable() {
        return isEmojiEnable;
    }

    public void banNumber() {
        isNumberEnable = false;
    }

    public void banLetter() {
        isLetterEnable = false;
    }

    public void banLetterUpperCase() {
        isLetterUpperCaseEnable = false;
    }

    public void banLetterLowerCase() {
        isLetterLowerCaseEnable = false;
    }

    public void banEmoji() {
        isEmojiEnable = false;
    }

}
