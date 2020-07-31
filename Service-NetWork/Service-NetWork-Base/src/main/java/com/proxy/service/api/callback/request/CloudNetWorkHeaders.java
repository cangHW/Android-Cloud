package com.proxy.service.api.callback.request;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : cangHX
 * on 2020/07/31  4:15 PM
 */
public final class CloudNetWorkHeaders {

    private final Map<String, String> headersMapper;

    private CloudNetWorkHeaders(Builder builder) {
        this.headersMapper = builder.headersMapper;
    }


    public static class Builder {
        private Map<String, String> headersMapper = new HashMap<>();

        public Builder addHeader(String key, String value) {
            this.headersMapper.put(key, value);
            return this;
        }

        public Builder removeHeader(String key) {
            this.headersMapper.remove(key);
            return this;
        }

        public CloudNetWorkHeaders build() {
            return new CloudNetWorkHeaders(this);
        }
    }
}
