package com.proxy.service.library.info;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudService;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.interfaces.IEditTextHelper;
import com.proxy.service.api.services.CloudUtilsEditTextService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.library.edittext.EditTextHelperImpl;

/**
 * @author: cangHX
 * on 2020/07/09  18:42
 */
@CloudService(serviceTag = CloudServiceTagUtils.UTILS_EDIT_TEXT)
public class UtilsEditTextServiceImpl implements CloudUtilsEditTextService {
    /**
     * 设置输入框属性
     *
     * @param editText : EditText
     * @return 辅助类
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-09 18:40
     */
    @NonNull
    @Override
    public IEditTextHelper with(EditText editText) {
        return new EditTextHelperImpl(editText);
    }

    /**
     * 弹出键盘
     *
     * @param editText : EditText
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-09 18:37
     */
    @Override
    public void showSoftInput(@NonNull EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, 0);
        }
    }

    /**
     * 隐藏键盘
     *
     * @param view : 任意 view
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-09 18:38
     */
    @Override
    public void hideSoftInput(View view) {
        if (view == null) {
            Activity activity = ContextManager.getCurrentActivity();
            if (activity == null) {
                return;
            }
            Window window = activity.getWindow();
            if (window == null) {
                return;
            }
            view = window.getDecorView();
        }

        try {
            view.requestFocus();
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Throwable throwable) {
            Logger.Debug(throwable);
        }
    }
}
