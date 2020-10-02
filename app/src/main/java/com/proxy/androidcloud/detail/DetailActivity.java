package com.proxy.androidcloud.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.base.BaseActivity;
import com.proxy.androidcloud.helper.AbstractDetailHelper;
import com.proxy.androidcloud.helper.DetailHelperType;
import com.proxy.androidcloud.helper.ListHelperType;

/**
 * @author : cangHX
 * on 2020/09/23  10:03 PM
 */
public class DetailActivity extends BaseActivity {

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
}
