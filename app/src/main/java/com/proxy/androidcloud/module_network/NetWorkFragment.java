package com.proxy.androidcloud.module_network;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.base.BaseApplication;
import com.proxy.androidcloud.base.BaseFragment;
import com.proxy.androidcloud.detail.ListActivity;
import com.proxy.androidcloud.helper.ListHelperType;
import com.proxy.androidcloud.module_network.request.KuaiDiBean;
import com.proxy.androidcloud.module_network.request.RequestService;
import com.proxy.androidcloud.module_network.request.WeatherBean;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.callback.request.CloudNetWorkCallback;
import com.proxy.service.api.callback.response.CloudNetWorkResponse;
import com.proxy.service.api.services.CloudNetWorkInitService;
import com.proxy.service.api.services.CloudNetWorkRequestService;
import com.proxy.service.api.tag.CloudServiceTagNetWork;
import com.proxy.service.api.utils.Logger;

/**
 * @author: cangHX
 * on 2020/07/06  10:21
 */
public class NetWorkFragment extends BaseFragment implements View.OnClickListener {

    public static final String BASE_URL_ID = "change_base_url";

    private CloudNetWorkRequestService mRequestService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_net_work, container, false);
        init(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRequestService = CloudSystem.getService(CloudServiceTagNetWork.NET_WORK_REQUEST);
    }

    private void init(View view) {
        view.findViewById(R.id.normal_request).setOnClickListener(this);
        view.findViewById(R.id.mock_request).setOnClickListener(this);
        view.findViewById(R.id.change_base_url_request).setOnClickListener(this);
        view.findViewById(R.id.download).setOnClickListener(this);
        view.findViewById(R.id.upload).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mRequestService == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.normal_request:
                normalRequest();
                break;
            case R.id.mock_request:
                mockRequest();
                break;
            case R.id.change_base_url_request:
                changeBaseUrlRequest();
                break;
            case R.id.download:
                ListActivity.launch(getContext(), ListHelperType.DOWNLOAD);
                break;
            case R.id.upload:
                ListActivity.launch(getContext(), ListHelperType.UPLOAD);
                break;
            default:
        }
    }

    private void normalRequest() {
        RequestService requestService = mRequestService.create(RequestService.class);
        requestService.normal("390011492112").enqueue(new CloudNetWorkCallback<KuaiDiBean>() {
            @Override
            public void onResponse(CloudNetWorkResponse<KuaiDiBean> response) {
                if (response.isSuccessful()) {
                    Logger.Error("onResponse 成功 : " + response.response().getCom());
                } else {
                    Logger.Error("onResponse 失败");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.Error("onFailure : " + t.getMessage());
            }
        });
    }

    private void mockRequest() {
        RequestService requestService = mRequestService.create(RequestService.class);
        requestService.mock("390011492112").enqueue(new CloudNetWorkCallback<KuaiDiBean>() {
            @Override
            public void onResponse(CloudNetWorkResponse<KuaiDiBean> response) {
                if (response.isSuccessful()) {
                    Logger.Error("onResponse 成功 : " + response.response().getCom());
                } else {
                    Logger.Error("onResponse 失败");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.Error("onFailure : " + t.getMessage());
            }
        });
    }

    private void changeBaseUrlRequest() {
        CloudNetWorkInitService initService = CloudSystem.getService(CloudNetWorkInitService.class);
        if (initService == null) {
            return;
        }
        initService.setBaseUrls(BASE_URL_ID, BaseApplication.BASE_URL_1);
        RequestService requestService = mRequestService.create(RequestService.class);
        requestService.baseUrl("北京", "json", "11ffd27d38deda622f51c9d314d46b17").enqueue(new CloudNetWorkCallback<WeatherBean>() {
            @Override
            public void onResponse(CloudNetWorkResponse<WeatherBean> response) {
                if (response.isSuccessful()) {
                    Logger.Error("onResponse 成功 : " + response.response().getMessage());
                } else {
                    Logger.Error("onResponse 失败");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Logger.Error("onFailure : " + t.getMessage());
            }
        });
    }
}
