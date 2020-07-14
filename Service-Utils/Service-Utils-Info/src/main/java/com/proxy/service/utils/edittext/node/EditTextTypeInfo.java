package com.proxy.service.utils.edittext.node;

/**
 * @author: cangHX
 * on 2020/07/10  09:57
 */
public class EditTextTypeInfo {

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

    public void setNumberEnable(boolean enable) {
        isNumberEnable = enable;
    }

    public void setLetterEnable(boolean enable) {
        isLetterEnable = enable;
    }

    public void setLetterUpperCaseEnable(boolean enable) {
        isLetterUpperCaseEnable = enable;
    }

    public void setLetterLowerCaseEnable(boolean enable) {
        isLetterLowerCaseEnable = enable;
    }

    public void setEmojiEnable(boolean enable) {
        isEmojiEnable = enable;
    }

}
