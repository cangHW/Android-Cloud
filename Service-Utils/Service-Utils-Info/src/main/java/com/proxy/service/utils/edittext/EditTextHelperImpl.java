package com.proxy.service.utils.edittext;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.CloudTextChangedCallback;
import com.proxy.service.api.interfaces.IEditTextAllowHelper;
import com.proxy.service.api.interfaces.IEditTextBanHelper;
import com.proxy.service.api.interfaces.IEditTextHelper;
import com.proxy.service.utils.edittext.helper.EditTextAllowHelperImpl;
import com.proxy.service.utils.edittext.helper.EditTextBanHelperImpl;
import com.proxy.service.utils.edittext.watcher.WatcherImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/07/10  17:53
 */
public class EditTextHelperImpl implements IEditTextHelper {

    private EditText mEditText;
    private List<CloudTextChangedCallback> mCallbacks = new ArrayList<>();

    public EditTextHelperImpl(EditText editText) {
        this.mEditText = editText;
        this.mEditText.addTextChangedListener(new WatcherImpl(editText, mCallbacks));
    }

    /**
     * 清除当前输入框的输入格式设置，危险，
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
        IEditTextBanHelper helper = createBanHelper();
        helper.banEmoji();
        return helper;
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
        IEditTextBanHelper helper = createBanHelper();
        helper.banNumber();
        return helper;
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
        IEditTextBanHelper helper = createBanHelper();
        helper.banLetter();
        return helper;
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
        IEditTextBanHelper helper = createBanHelper();
        helper.banLetterLowerCase();
        return helper;
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
        IEditTextBanHelper helper = createBanHelper();
        helper.banLetterUpperCase();
        return helper;
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
        IEditTextBanHelper helper = createBanHelper();
        helper.banDigits(digits);
        return helper;
    }

    private IEditTextBanHelper createBanHelper() {
        return new EditTextBanHelperImpl(mEditText, mCallbacks);
    }
}
