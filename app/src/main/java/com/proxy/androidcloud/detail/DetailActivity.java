package com.proxy.androidcloud.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.base.BaseActivity;
import com.proxy.androidcloud.helper.HelperType;

/**
 * @author : cangHX
 * on 2020/08/13  10:03 PM
 */
public class DetailActivity extends BaseActivity {

    public static void launch(Context context, HelperType type) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(HELPER_TYPE, type);
        context.startActivity(intent);
    }

    private static final String HELPER_TYPE = "HelperType";

    private HelperType mHelperType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Object object = getIntent().getSerializableExtra(HELPER_TYPE);
        if (object == null) {
            return;
        }
        mHelperType = (HelperType) object;
        initView();
    }

    private void initView() {
        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        DetailAdapter adapter = new DetailAdapter();
        adapter.setData(mHelperType.create());
        recycler.setAdapter(adapter);
    }
}
