package com.proxy.androidcloud;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.androidcloud.base.BaseActivity;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.request.annotations.CloudUiCheckBoolean;
import com.proxy.service.api.request.annotations.CloudUiCheckString;
import com.proxy.service.api.request.annotations.CloudUiCheckStrings;
import com.proxy.service.api.edittext.CloudTextChangedCallback;
import com.proxy.service.api.services.CloudUiFieldCheckService;
import com.proxy.service.api.services.CloudUtilsEditTextService;
import com.proxy.service.api.tag.CloudServiceTagUi;
import com.proxy.service.api.tag.CloudServiceTagUtils;

/**
 * @author: cangHX
 * on 2020/07/08  20:35
 */
@SuppressWarnings("FieldCanBeLocal")
public class LoginActivity extends BaseActivity implements CloudTextChangedCallback, CompoundButton.OnCheckedChangeListener {

    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";
    private static final String PROTOCOL = "protocol";

    @CloudUiCheckStrings({
            @CloudUiCheckString(markId = ACCOUNT, notEmpty = true, message = "请输入用户名"),
            @CloudUiCheckString(markId = ACCOUNT, notBlank = true, message = "用户名不能为空格"),
            @CloudUiCheckString(markId = ACCOUNT, maxLength = 8, notBlank = true, stringId = R.string.long_account),
            @CloudUiCheckString(markId = ACCOUNT, minLength = 4, notBlank = true, message = "用户名太短"),
            @CloudUiCheckString(markId = ACCOUNT, notWithRegex = "[01abc]", message = "用户名不能包含 01abc")
    })
    private String mAccountText;

    @CloudUiCheckStrings({
            @CloudUiCheckString(markId = PASSWORD, notEmpty = true, message = "请输入密码"),
            @CloudUiCheckString(markId = PASSWORD, notBlank = true, message = "密码不能为空格"),
            @CloudUiCheckString(markId = PASSWORD, maxLength = 4, minLength = 4, notBlank = true, message = "密码不符合4位要求"),
            @CloudUiCheckString(markId = PASSWORD, shouldWithRegex = "[1]", message = "密码必须包含 1"),
            @CloudUiCheckString(markId = PASSWORD, notWithRegex = "(1111|1234|1222|1333|1444|1324|1432|1423)", message = "密码太简单")
    })
    private String mPasswordText;

    @CloudUiCheckBoolean(markId = PROTOCOL, isValue = true, message = "需要同意用户协议")
    private boolean mProtocolReady;

    private EditText mInputAccountView;
    private EditText mInputPasswordView;
    private CloudUiFieldCheckService mFieldCheckService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mInputAccountView = findViewById(R.id.input_account);
        mInputPasswordView = findViewById(R.id.input_password);
        CheckBox checkView = findViewById(R.id.checkView);
        checkView.setOnCheckedChangeListener(this);

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

        mFieldCheckService = CloudSystem.getService(CloudServiceTagUi.UI_FIELD_CHECK);
        if (mFieldCheckService != null) {
            //初始化
            mFieldCheckService.init(this);
        }
        //设置全局吐司
//        mService.setGlobalErrorToastCallback();
        //设置当前吐司
//        mService.setErrorToastCallback();
    }

    public void onClick(View view) {
        if (mFieldCheckService != null) {
            mFieldCheckService
                    .check(ACCOUNT)
                    .check(PASSWORD)
                    .check(PROTOCOL)
                    .runUi(() -> {
                        MainActivity.launch(LoginActivity.this);
                        finish();
                    });
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mProtocolReady = isChecked;
    }
}
