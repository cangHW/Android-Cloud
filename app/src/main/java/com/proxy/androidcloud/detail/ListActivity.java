package com.proxy.androidcloud.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.base.BaseActivity;
import com.proxy.androidcloud.helper.ListHelperType;

/**
 * @author : cangHX
 * on 2020/08/13  10:03 PM
 */
public class ListActivity extends BaseActivity {

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
}
