package com.proxy.androidcloud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.callback.CloudUiEventCallback;
import com.proxy.service.api.services.CloudUiTabHostService;
import com.proxy.service.api.tag.CloudServiceTagUi;

public class MainActivity extends AppCompatActivity implements CloudUiEventCallback {

    private static final String TAG = "ssss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout contentLayout = findViewById(R.id.content_layout);

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

    /**
     * 接收到回调
     *
     * @param tabIndex : 标示发次本次事件的tab
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
        }
    }
}
