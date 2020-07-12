package com.proxy.service.utils.edittext.watcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.CloudTextChangedCallback;
import com.proxy.service.api.utils.Logger;

import java.util.Set;

/**
 * @author: cangHX
 * on 2020/07/10  19:13
 */
public class WatcherImpl implements TextWatcher {

    private Set<CloudTextChangedCallback> mCallbacks;
    private EditText mEditText;
    private String mBeforeText;

    public WatcherImpl(@NonNull EditText editText, @NonNull Set<CloudTextChangedCallback> list) {
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
