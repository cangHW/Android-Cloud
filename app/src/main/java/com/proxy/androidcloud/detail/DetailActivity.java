package com.proxy.androidcloud.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.proxy.androidcloud.base.BaseActivity;
import com.proxy.androidcloud.helper.AbstractDetailHelper;
import com.proxy.androidcloud.helper.DetailHelperType;
import com.proxy.service.api.context.LifecycleState;
import com.proxy.service.api.lifecycle.CloudActivityLifecycleListener;

/**
 * @author : cangHX
 * on 2020/09/23  10:03 PM
 */
public class DetailActivity extends BaseActivity implements CloudActivityLifecycleListener {

    public static void launch(Context context, DetailHelperType type) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(HELPER_TYPE, type);
        context.startActivity(intent);
    }

    private static final String HELPER_TYPE = "HelperType";

    private DetailHelperType mDetailHelperType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Object object = getIntent().getSerializableExtra(HELPER_TYPE);
        if (object == null) {
            return;
        }
        mDetailHelperType = (DetailHelperType) object;
        showTitle(mDetailHelperType.serviceName());
        AbstractDetailHelper helper = mDetailHelperType.create();
        setContentView(helper.getLayoutId());
        helper.init(this);
    }

    /**
     * 回调生命周期
     *
     * @param activity       : 准备观察生命周期的对象
     * @param lifecycleState : 生命周期状态回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/07/15  2:11 PM
     */
    @Override
    public void onLifecycleChanged(Activity activity, LifecycleState lifecycleState) {

    }
}
