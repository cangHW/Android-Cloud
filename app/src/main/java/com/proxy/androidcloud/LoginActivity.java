package com.proxy.androidcloud;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.proxy.androidcloud.base.BaseActivity;

/**
 * @author: cangHX
 * on 2020/07/08  20:35
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.launch(this);
    }
}
