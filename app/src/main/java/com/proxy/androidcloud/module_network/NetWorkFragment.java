package com.proxy.androidcloud.module_network;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.base.BaseFragment;
import com.proxy.androidcloud.module_network.request.TestBean;
import com.proxy.androidcloud.module_network.request.TestRequestService;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.callback.CloudUiLifeCallback;
import com.proxy.service.api.callback.request.CloudNetWorkCallback;
import com.proxy.service.api.callback.response.CloudNetWorkResponse;
import com.proxy.service.api.services.CloudNetWorkRequestService;
import com.proxy.service.api.tag.CloudServiceTagNetWork;
import com.proxy.service.api.utils.Logger;

/**
 * @author: cangHX
 * on 2020/07/06  10:21
 */
public class NetWorkFragment extends BaseFragment implements View.OnClickListener {

    private AppCompatButton mRequestView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_net_work, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mRequestView = view.findViewById(R.id.request);
        mRequestView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        CloudNetWorkRequestService service = CloudSystem.getService(CloudServiceTagNetWork.NET_WORK_REQUEST);
        if (service != null) {
            TestRequestService requestService = service.create(TestRequestService.class);
            requestService.test().enqueue(new CloudNetWorkCallback<TestBean>() {
                @Override
                public void onResponse(CloudNetWorkResponse<TestBean> response) {
                    Logger.Error("onResponse : " + response.response().name);
                }

                @Override
                public void onFailure(Throwable t) {
                    Logger.Error("onFailure : " + t.getMessage());
                }
            });
        }
    }
}
