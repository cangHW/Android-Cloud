package com.proxy.androidcloud.module_network.request;

import com.proxy.androidcloud.base.BaseApplication;
import com.proxy.androidcloud.module_network.NetWorkFragment;
import com.proxy.service.api.request.annotations.CloudNetWorkBaseUrl;
import com.proxy.service.api.request.annotations.CloudNetWorkBaseUrlId;
import com.proxy.service.api.request.annotations.CloudNetWorkField;
import com.proxy.service.api.request.annotations.CloudNetWorkGet;
import com.proxy.service.api.callback.request.CloudNetWorkCall;

/**
 * @author : cangHX
 * on 2020/08/03  6:17 PM
 */
public interface RequestService {

    @CloudNetWorkBaseUrl(BaseApplication.BASE_URL)
    @CloudNetWorkGet(BaseApplication.PATH_URL1)
    CloudNetWorkCall<KuaiDiBean> normal(@CloudNetWorkField("postid") String postid);

    @CloudNetWorkBaseUrl(BaseApplication.BASE_URL)
    @CloudNetWorkGet(BaseApplication.PATH_URL2)
    CloudNetWorkCall<KuaiDiBean> mock( @CloudNetWorkField("postid") String postid);

    @CloudNetWorkBaseUrlId(NetWorkFragment.BASE_URL_ID)
    @CloudNetWorkGet(BaseApplication.PATH_URL_1)
    CloudNetWorkCall<WeatherBean> baseUrl(
            @CloudNetWorkField("location") String location,
            @CloudNetWorkField("output") String output,
            @CloudNetWorkField("ak") String ak
    );
}
