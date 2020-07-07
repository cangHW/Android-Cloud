package com.proxy.androidcloud.base;

import android.app.Application;

import com.proxy.service.api.CloudSystem;

/**
 * @author: cangHX
 * on 2020/07/06  14:46
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CloudSystem.init(this, true);
    }
}
