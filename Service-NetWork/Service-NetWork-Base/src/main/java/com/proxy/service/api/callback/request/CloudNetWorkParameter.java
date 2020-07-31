package com.proxy.service.api.callback.request;

/**
 * @author : cangHX
 * on 2020/07/31  4:34 PM
 */
public final class CloudNetWorkParameter {


    private CloudNetWorkParameter(Builder builder) {

    }


    public static class Builder {


        public CloudNetWorkParameter build() {
            return new CloudNetWorkParameter(this);
        }
    }
}
