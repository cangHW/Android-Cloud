package com.proxy.service.utils.edittext.watcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.proxy.service.api.edittext.CloudTextChangedCallback;
import com.proxy.service.api.utils.Logger;

import java.util.Set;

/**
 * @author: cangHX
 * on 2020/07/10  19:13
 */
public class WatcherImpl implements TextWatcher {

    private final Set<CloudTextChangedCallback> mCallbacks;
    private final EditText mEditText;
    private String mBeforeText;

    public WatcherImpl(EditText editText, Set<CloudTextChangedCallback> list) {
        this.mEditText = editText;
        this.mCallbacks = list;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (s != null) {
            mBeforeText = s.toString();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mCallbacks == null) {
            return;
        }
        for (CloudTextChangedCallback callback : mCallbacks) {
            if (callback == null) {
                continue;
            }
            try {
                callback.onChanged(mEditText, s, mBeforeText);
            } catch (Throwable throwable) {
                Logger.Debug(throwable);
            }
        }
    }
}
