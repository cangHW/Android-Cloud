package com.proxy.service.utils.edittext;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.proxy.service.api.edittext.CloudTextChangedCallback;
import com.proxy.service.api.edittext.IEditTextAllowHelper;
import com.proxy.service.api.edittext.IEditTextBanHelper;
import com.proxy.service.api.edittext.IEditTextHelper;
import com.proxy.service.utils.edittext.helper.EditTextAllowHelperImpl;
import com.proxy.service.utils.edittext.helper.EditTextBanHelperImpl;
import com.proxy.service.utils.edittext.watcher.WatcherImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: cangHX
 * on 2020/07/10  17:53
 */
public class EditTextHelperImpl implements IEditTextHelper {

    private final EditText mEditText;
    private final Set<CloudTextChangedCallback> mCallbacks = new HashSet<>();

    public EditTextHelperImpl(EditText editText) {
        this.mEditText = editText;
        this.mEditText.addTextChangedListener(new WatcherImpl(editText, mCallbacks));
    }

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
    @Override
    public IEditTextAllowHelper clearInputType() {
        return new EditTextAllowHelperImpl(mEditText, mCallbacks);
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
    public IEditTextHelper addTextChangedCallback(@NonNull CloudTextChangedCallback callback) {
        this.mCallbacks.add(callback);
        return this;
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
        return createBanHelper().banEmoji();
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
        return createBanHelper().banNumber();
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
        return createBanHelper().banLetter();
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
        return createBanHelper().banLetterLowerCase();
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
        return createBanHelper().banLetterUpperCase();
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
        return createBanHelper().banDigits(digits);
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
        return createBanHelper().banMatcher(regex);
    }

    /**
     * 创建禁止 helper 类
     */
    private IEditTextBanHelper createBanHelper() {
        return new EditTextBanHelperImpl(mEditText, mCallbacks);
    }
}
