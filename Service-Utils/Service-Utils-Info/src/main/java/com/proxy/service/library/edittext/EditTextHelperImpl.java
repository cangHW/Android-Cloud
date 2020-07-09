package com.proxy.service.library.edittext;

import android.widget.EditText;

import com.proxy.service.api.interfaces.IEditTextHelper;

/**
 * @author: cangHX
 * on 2020/07/09  19:06
 */
public class EditTextHelperImpl implements IEditTextHelper {

    private EditText mEditText;

    public EditTextHelperImpl(EditText editText) {
        this.mEditText = editText;
    }

}
