package com.proxy.androidcloud.module_network.request;

import com.proxy.service.api.annotations.CloudNetWorkGet;
import com.proxy.service.api.callback.request.CloudNetWorkCall;

/**
 * @author : cangHX
 * on 2020/08/03  6:17 PM
 */
public interface TestRequestService {

    @CloudNetWorkGet("sss")
    CloudNetWorkCall<TestBean> test();

}
