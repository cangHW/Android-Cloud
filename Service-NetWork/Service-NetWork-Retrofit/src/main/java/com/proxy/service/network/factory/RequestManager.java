package com.proxy.service.network.factory;

import com.proxy.service.api.callback.request.NetWorkCallback;

/**
 * @author : cangHX
 * on 2020/07/31  3:19 PM
 */
public class RequestManager implements NetWorkCallback {

    private RequestManager(Builder builder) {

    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {


        public RequestManager build() {
            return new RequestManager(this);
        }
    }
}
