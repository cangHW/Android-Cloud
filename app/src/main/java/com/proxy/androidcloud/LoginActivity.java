package com.proxy.androidcloud;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.proxy.androidcloud.base.BaseActivity;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.annotations.CloudUiCheckString;
import com.proxy.service.api.annotations.CloudUiCheckStrings;
import com.proxy.service.api.services.CloudUiFieldCheckService;
import com.proxy.service.api.tag.CloudServiceTagUi;

/**
 * @author: cangHX
 * on 2020/07/08  20:35
 */
@SuppressWarnings("FieldCanBeLocal")
public class LoginActivity extends BaseActivity {

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
        mAccountText = mInputAccountView.getText().toString();
        mPasswordText = mInputPasswordView.getText().toString();

        if (mService != null) {
            mService.of(ACCOUNT, mAccountText)
                    .of(PASSWORD, mPasswordText)
                    .runUi(() -> MainActivity.launch(LoginActivity.this));
        }

    }
}
