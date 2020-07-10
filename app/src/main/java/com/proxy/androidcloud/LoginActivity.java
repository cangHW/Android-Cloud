package com.proxy.androidcloud;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.androidcloud.base.BaseActivity;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.annotations.CloudUiCheckString;
import com.proxy.service.api.annotations.CloudUiCheckStrings;
import com.proxy.service.api.callback.CloudTextChangedCallback;
import com.proxy.service.api.services.CloudUiFieldCheckService;
import com.proxy.service.api.services.CloudUtilsEditTextService;
import com.proxy.service.api.tag.CloudServiceTagUi;
import com.proxy.service.api.tag.CloudServiceTagUtils;

/**
 * @author: cangHX
 * on 2020/07/08  20:35
 */
@SuppressWarnings("FieldCanBeLocal")
public class LoginActivity extends BaseActivity implements CloudTextChangedCallback {

    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";

    @CloudUiCheckStrings({
            @CloudUiCheckString(markId = ACCOUNT, notEmpty = true, message = "请输入用户名"),
            @CloudUiCheckString(markId = ACCOUNT, notBlank = true, message = "用户名不能为空格"),
            @CloudUiCheckString(markId = ACCOUNT, maxLength = 8, notBlank = true, stringId = R.string.long_account),
            @CloudUiCheckString(markId = ACCOUNT, minLength = 4, notBlank = true, message = "用户名太短")
    })
    private String mAccountText;

    @CloudUiCheckStrings({
            @CloudUiCheckString(markId = PASSWORD, notEmpty = true, message = "请输入密码"),
            @CloudUiCheckString(markId = PASSWORD, notBlank = true, message = "密码不能为空格"),
            @CloudUiCheckString(markId = PASSWORD, maxLength = 4, minLength = 4, notBlank = true, message = "密码不符合4位要求")
    })
    private String mPasswordText = null;

    private EditText mInputAccountView;
    private EditText mInputPasswordView;
    private CloudUiFieldCheckService mService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mInputAccountView = findViewById(R.id.input_account);
        mInputPasswordView = findViewById(R.id.input_password);

        CloudUtilsEditTextService editTextService = CloudSystem.getService(CloudServiceTagUtils.UTILS_EDIT_TEXT);
        if (editTextService != null) {
            editTextService
                    .with(mInputAccountView)
                    .clearInputType()
                    .allowLetter()
                    .allowNumber()
                    .addTextChangedCallback(this);
            editTextService
                    .with(mInputPasswordView)
                    .clearInputType()
                    .allowNumber()
                    .addTextChangedCallback(this);
        }


        mService = CloudSystem.getService(CloudServiceTagUi.UI_FIELD_CHECK);
        if (mService != null) {
            //初始化
            mService.init(LoginActivity.class);
        }
        //设置全局吐司
//        mService.setGlobalErrorToastCallback();
        //设置当前吐司
//        mService.setErrorToastCallback();
    }

    public void onClick(View view) {
        if (mService != null) {
            mService.of(ACCOUNT, mAccountText)
                    .of(PASSWORD, mPasswordText)
                    .runUi(() -> MainActivity.launch(LoginActivity.this));
        }
    }

    /**
     * 文字变化
     *
     * @param view     : 发生变化的 view
     * @param newValue : 改变后的内容
     * @param oldValue : 改变前的内容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-09 18:27
     */
    @Override
    public void onChanged(@NonNull View view, @NonNull Editable newValue, @NonNull String oldValue) {
        if (view == mInputAccountView) {
            mAccountText = newValue.toString();
        } else if (view == mInputPasswordView) {
            mPasswordText = newValue.toString();
        }
    }
}