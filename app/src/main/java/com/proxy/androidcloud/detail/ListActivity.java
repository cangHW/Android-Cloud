package com.proxy.androidcloud.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.base.BaseActivity;
import com.proxy.androidcloud.helper.ListHelperType;
import com.proxy.service.api.context.LifecycleState;
import com.proxy.service.api.lifecycle.CloudActivityLifecycleListener;

/**
 * @author : cangHX
 * on 2020/08/13  10:03 PM
 */
public class ListActivity extends BaseActivity implements CloudActivityLifecycleListener {

    public static void launch(Context context, ListHelperType type) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra(HELPER_TYPE, type);
        context.startActivity(intent);
    }

    private static final String HELPER_TYPE = "HelperType";

    private ListHelperType mListHelperType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Object object = getIntent().getSerializableExtra(HELPER_TYPE);
        if (object == null) {
            return;
        }
        mListHelperType = (ListHelperType) object;
        initView();
    }

    private void initView() {
        showTitle(mListHelperType.serviceName());

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        ListAdapter adapter = new ListAdapter();
        adapter.setData(mListHelperType.create());
        recycler.setAdapter(adapter);
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
