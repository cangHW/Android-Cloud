package com.proxy.service.utils.info;

import android.app.Activity;
import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.annotations.CloudService;
import com.proxy.service.api.context.ContextManager;
import com.proxy.service.api.interfaces.IEditTextHelper;
import com.proxy.service.api.services.CloudUtilsEditTextService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.utils.edittext.EditTextHelperImpl;

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
    public IEditTextHelper with(@NonNull EditText editText) {
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
    public void hideSoftInput(@Nullable View view) {
        if (view instanceof EditText) {
            view = getOtherView(view);
        }

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

    /**
     * 隐藏输入内容，用点号替换文字
     *
     * @param editText : EditText
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-11 10:36
     */
    @Override
    public void hideInputContent(@NonNull EditText editText) {
        editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        editText.setSelection(editText.getText().length());
    }

    /**
     * 显示输入内容
     *
     * @param editText : EditText
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-11 10:36
     */
    @Override
    public void showInputContent(@NonNull EditText editText) {
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText.setSelection(editText.getText().length());
    }

    private View getOtherView(View view) {
        while (view.getParent() instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View currentView = viewGroup.getChildAt(i);
                boolean flag = currentView instanceof EditText;
                if (!flag) {
                    return currentView;
                }
            }
            view = viewGroup;
        }
        return null;
    }
}
