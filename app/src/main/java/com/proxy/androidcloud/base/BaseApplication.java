package com.proxy.androidcloud.base;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.proxy.androidcloud.module_network.NetWorkFragment;
import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.request.base.CloudNetWorkInterceptor;
import com.proxy.service.api.request.base.CloudNetWorkMock;
import com.proxy.service.api.callback.request.CloudNetWorkCall;
import com.proxy.service.api.callback.request.CloudNetWorkGlobalCallback;
import com.proxy.service.api.callback.response.CloudNetWorkResponse;
import com.proxy.service.api.request.method.CloudNetWorkHttpUrl;
import com.proxy.service.api.request.method.CloudNetWorkRequest;
import com.proxy.service.api.services.CloudNetWorkInitService;
import com.proxy.service.api.tag.CloudServiceTagNetWork;
import com.proxy.service.api.utils.Logger;

/**
 * @author: cangHX
 * on 2020/07/06  14:46
 */
public class BaseApplication extends Application {

    String xxx = "{\"message\":\"mock 数据\",\"nu\":\"mock 数据\",\"ischeck\":\"mock 数据\",\"com\":\"mock 数据\",\"status\":\"mock 数据\",\"condition\":\"mock 数据\",\"state\":\"mock 数据\",\"data\":[{\"time\":\"mock 数据\",\"context\":\"mock 数据\",\"ftime\":\"mock 数据\"}]}";

    public static final String BASE_URL = "https://www.kuaidi100.com/";
    public static final String PATH_URL1 = "query?type=quanfengkuaidi";
    public static final String PATH_URL2 = "query?type=qwwwwww";

    public static final String BASE_URL_1 = "http://api.map.baidu.com/";
    public static final String PATH_URL_1 = "telematics/v3/weather";

    @Override
    public void onCreate() {
        super.onCreate();
        CloudSystem.init(this, true);

        CloudNetWorkInitService initService = CloudSystem.getService(CloudServiceTagNetWork.NET_WORK_INIT);
        if (initService != null) {
            initService
                    .setBaseUrl("https://www.baidu.com")
                    .setBaseUrls(NetWorkFragment.BASE_URL_ID, "https://www.baidu.com")
                    .setMock(mock)
                    .setGlobalRequestCallback(globalCallback)
                    .addNetWorkInterceptor(logInterceptor)
                    .build();
        }
    }

    private CloudNetWorkMock mock = new CloudNetWorkMock() {
        @Override
        public String loadMock(CloudNetWorkHttpUrl httpUrl) {
            String url = httpUrl.url();
            if (url.equals(BASE_URL + PATH_URL2)) {
                return xxx;
            }
            return null;
        }
    };

    private CloudNetWorkInterceptor logInterceptor = new CloudNetWorkInterceptor() {
        @Override
        public <T> CloudNetWorkResponse<T> intercept(@NonNull Chain chain) {
            CloudNetWorkRequest request = chain.request();
            Logger.Error("NetWork request url : " + request.httpUrl().url());
            Logger.Error("NetWork request params : " + request.parameter().getParameterMap());
            CloudNetWorkResponse<T> proceed = chain.proceed(request);
            Object object = proceed.response();
            if (object instanceof String) {
                Logger.Error("NetWork response : " + object);
            }
            return proceed;
        }
    };

    private CloudNetWorkGlobalCallback globalCallback = new CloudNetWorkGlobalCallback() {
        @Override
        public void onStart(CloudNetWorkCall<?> call) {
            Toast.makeText(BaseApplication.this, "开始请求", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onResponse(CloudNetWorkCall<?> call, CloudNetWorkResponse<?> response) {
            return false;
        }

        @Override
        public boolean onFailure(CloudNetWorkCall<?> call, Throwable t) {
            return false;
        }

        @Override
        public void onFinish(CloudNetWorkCall<?> call) {
            Toast.makeText(BaseApplication.this, "结束请求", Toast.LENGTH_SHORT).show();
        }
    };

}
