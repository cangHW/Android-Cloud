package com.proxy.service.api.method;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : cangHX
 * on 2020/07/31  4:34 PM
 */
public final class CloudNetWorkParameter {

    private final Map<String, String> mParameterMapper;

    private CloudNetWorkParameter(Builder builder) {
        this.mParameterMapper = builder.mParameterMapper;
    }

    public Map<String, String> getParameterMap() {
        return mParameterMapper;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static class Builder {

        private Map<String, String> mParameterMapper = new HashMap<>();

        public Builder() {
        }

        private Builder(CloudNetWorkParameter parameter) {
            this.mParameterMapper = parameter.mParameterMapper;
            if (this.mParameterMapper == null) {
                this.mParameterMapper = new HashMap<>();
            }
        }

        public Builder put(String key, String value) {
            mParameterMapper.put(key, value);
            return this;
        }

        public Map<String, String> getMap() {
            return mParameterMapper;
        }

        public CloudNetWorkParameter build() {
            return new CloudNetWorkParameter(this);
        }
    }
}
