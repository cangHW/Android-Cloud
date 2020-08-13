package com.proxy.androidcloud;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.proxy.androidcloud.base.BaseActivity;
import com.proxy.androidcloud.listener.onViewClickListener;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.callback.CloudUiEventCallback;
import com.proxy.service.api.services.CloudUiTabHostService;
import com.proxy.service.api.tag.CloudServiceTagUi;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements CloudUiEventCallback {

    private static final String TAG = "ssss";

    public static void launch(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    private List<onViewClickListener> mClickListeners = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup contentLayout = findViewById(R.id.content_layout);

        LinearLayout bottomLayout = findViewById(R.id.bottom_layout);

        CloudUiTabHostService service = CloudSystem.getService(CloudServiceTagUi.UI_TAB_HOST);
        if (service != null) {
            service.setActivity(this)
                    .setContentSpace(contentLayout)
                    .setTabSpace(bottomLayout)
                    .addEventCallback(this)
                    .setSelect(0)
                    .showWithTag("main");
        }
    }

    public void onClick(View view) {
        Object tag = view.getTag();
        if (tag == null) {
            return;
        }
        for (onViewClickListener listener : mClickListeners) {
            if (String.valueOf(tag).equals(listener.tag())) {
                listener.onClick(view);
            }
        }
    }

    /**
     * 接收到回调
     *
     * @param tabIndex : 标示发送本次事件的tab
     * @param tag      : 保留对象，开发者可以自定义设置
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:15
     */
    @Override
    public void onCall(int tabIndex, Object tag) {
        if (tag instanceof String) {
            String message = (String) tag;
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        } else if (tag instanceof onViewClickListener) {
            mClickListeners.add((onViewClickListener) tag);
        }
    }
}
