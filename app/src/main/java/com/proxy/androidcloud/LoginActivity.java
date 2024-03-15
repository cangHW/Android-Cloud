package com.proxy.androidcloud;

import android.Manifest;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.androidcloud.base.BaseActivity;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.action.Action;
import com.proxy.service.api.annotations.CloudUiCheckBoolean;
import com.proxy.service.api.annotations.CloudUiCheckString;
import com.proxy.service.api.annotations.CloudUiCheckStrings;
import com.proxy.service.api.bitmap.CloudCaptchaInfo;
import com.proxy.service.api.edittext.CloudTextChangedCallback;
import com.proxy.service.api.impl.CloudUiCheckStringInfo;
import com.proxy.service.api.services.CloudUiFieldCheckService;
import com.proxy.service.api.services.CloudUtilsBitmapService;
import com.proxy.service.api.services.CloudUtilsEditTextService;
import com.proxy.service.api.services.CloudUtilsPermissionService;
import com.proxy.service.api.tag.CloudServiceTagUi;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.utils.Logger;

/**
 * @author: cangHX
 * on 2020/07/08  20:35
 */
@SuppressWarnings("FieldCanBeLocal")
public class LoginActivity extends BaseActivity implements CloudTextChangedCallback, CompoundButton.OnCheckedChangeListener {

    private static final Logger logger = Logger.create(LoginActivity.class.getName());

    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";
    private static final String CAPTCHA = "captcha";
    private static final String PROTOCOL = "protocol";

    @CloudUiCheckStrings({
            @CloudUiCheckString(markId = ACCOUNT, notEmpty = true, message = "请输入用户名"),
            @CloudUiCheckString(markId = ACCOUNT, notBlank = true, message = "用户名不能为空格"),
//            @CloudUiCheckString(markId = ACCOUNT, maxLength = 8, notBlank = true, stringId = R.string.long_account),
            @CloudUiCheckString(markId = ACCOUNT, minLength = 4, notBlank = true, message = "用户名太短"),
            @CloudUiCheckString(markId = ACCOUNT, notWithRegex = "[01abc]", message = "用户名不能包含 01abc")
    })
    private String mAccountText = "";

    @CloudUiCheckStrings({
            @CloudUiCheckString(markId = PASSWORD, notEmpty = true, message = "请输入密码"),
            @CloudUiCheckString(markId = PASSWORD, notBlank = true, message = "密码不能为空格"),
            @CloudUiCheckString(markId = PASSWORD, maxLength = 4, minLength = 4, notBlank = true, message = "密码不符合4位要求"),
            @CloudUiCheckString(markId = PASSWORD, shouldWithRegex = "[1]", message = "密码必须包含 1"),
            @CloudUiCheckString(markId = PASSWORD, notWithRegex = "(1111|1234|1222|1333|1444|1324|1432|1423)", message = "密码太简单")
    })
    private String mPasswordText = "";

    private String mCaptchaText = "";

    @CloudUiCheckBoolean(markId = PROTOCOL, isValue = true, message = "需要同意用户协议")
    private boolean mProtocolReady = true;

    private EditText mInputAccountView;
    private EditText mInputPasswordView;
    private EditText mInputCaptchaView;
    private ImageView mCaptchaImageView;
    private CheckBox mProtocolCheckView;

    private CloudUiFieldCheckService mFieldCheckService;
    private CloudUtilsBitmapService mBitmapService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mInputAccountView = findViewById(R.id.input_account);
        mInputPasswordView = findViewById(R.id.input_password);
        mInputCaptchaView = findViewById(R.id.input_captcha);
        mCaptchaImageView = findViewById(R.id.captcha_image);
        mCaptchaImageView.setOnClickListener(this::onClick);
        mProtocolCheckView = findViewById(R.id.checkView);
        mProtocolCheckView.setOnCheckedChangeListener(this);

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
            editTextService
                    .with(mInputCaptchaView)
                    .clearInputType()
                    .allowNumber()
                    .allowLetter()
                    .addTextChangedCallback(this);
        }

        mBitmapService = CloudSystem.getService(CloudUtilsBitmapService.class);

        mFieldCheckService = CloudSystem.getService(CloudServiceTagUi.UI_FIELD_CHECK);
        if (mFieldCheckService != null) {
            //初始化
            mFieldCheckService.init(this);

            mFieldCheckService.setConditions(1, CloudUiCheckStringInfo.builder(CAPTCHA)
                    .setNotEmpty(true)
                    .setMessage("图形验证码不能为空")
                    .build());

            mFieldCheckService.setConditions(2, CloudUiCheckStringInfo.builder(CAPTCHA)
                    .setMaxLength(4)
                    .setMinLength(4)
                    .setMessage("图形验证码长度不符合要求")
                    .build());
        }
        //设置全局吐司
//        mService.setGlobalErrorToastCallback();
        //设置当前吐司
//        mService.setErrorToastCallback();

        onClick(mCaptchaImageView);

        permission();
    }

    /**
     * 设置默认数据
     */
    private void setNormal(String captcha) {
        mInputAccountView.setText("oykkk");
        mInputPasswordView.setText("1233");
        mInputCaptchaView.setText(captcha);
        mProtocolCheckView.setChecked(true);
    }

    public void onClick(View view) {
        if (view == mCaptchaImageView) {
            //刷新图形验证码
            if (mBitmapService == null) {
                return;
            }
            CloudCaptchaInfo captchaInfo = mBitmapService.captcha(240, 100, "", 40);
            mCaptchaImageView.setImageBitmap(captchaInfo.getBitmap());
            if (mFieldCheckService != null) {
                mFieldCheckService.setConditions(3, CloudUiCheckStringInfo.builder(CAPTCHA)
                        .setShouldWithRegex("(" + captchaInfo.getKey() + ")")
                        .setMessage("图形验证码错误")
                        .build());
            }
            //刷新设置默认数据
            setNormal(captchaInfo.getKey());
            return;
        }

        if (mFieldCheckService != null) {
            //登录
            mFieldCheckService
                    .check(ACCOUNT)
                    .check(PASSWORD)
                    .check(CAPTCHA, mCaptchaText)
                    .check(PROTOCOL)
                    .runMain(() -> {
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
        } else if (view == mInputCaptchaView) {
            mCaptchaText = newValue.toString();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mProtocolReady = isChecked;
    }

    /**
     * 申请需要的权限
     */
    private void permission() {
        CloudUtilsPermissionService permissionService = CloudSystem.getService(CloudUtilsPermissionService.class);
        if (permissionService != null) {
            permissionService.create()
                    .addPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .onGranted(new Action<String[]>() {
                        @Override
                        public void onAction(String[] strings) {
                            //成功
                            for (String string : strings) {
                                logger.error("onGranted : " + string);
                            }
                        }
                    })
                    .onRationale(new Action<String[]>() {
                        @Override
                        public void onAction(String[] strings) {
                            //拒绝并且不再弹出
                            for (String string : strings) {
                                logger.error("onRationale : " + string);
                            }
                        }
                    })
                    .onDenied(new Action<String[]>() {
                        @Override
                        public void onAction(String[] strings) {
                            //拒绝
                            for (String string : strings) {
                                logger.error("onDenied : " + string);
                            }
                        }
                    })
                    .request();
        }
    }

}
