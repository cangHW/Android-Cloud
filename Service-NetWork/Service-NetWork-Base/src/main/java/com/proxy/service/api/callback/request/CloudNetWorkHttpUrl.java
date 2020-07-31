package com.proxy.service.api.callback.request;

import android.text.TextUtils;

/**
 * @author : cangHX
 * on 2020/07/31  4:13 PM
 */
public final class CloudNetWorkHttpUrl {

    private final String baseUrl;
    private final String pathUrl;
    private final String url;

    private CloudNetWorkHttpUrl(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.pathUrl = builder.pathUrl;
        this.url = builder.url;
    }

    public String url() {
        return url;
    }

    public static class Builder {
        private String baseUrl;
        private String pathUrl;
        private String url;

        public Builder() {
        }

        public Builder(CloudNetWorkHttpUrl httpUrl) {
            this.baseUrl = httpUrl.baseUrl;
            this.pathUrl = httpUrl.pathUrl;
            this.url = httpUrl.url;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            if (!TextUtils.isEmpty(baseUrl)) {
                url = "";
            }
            return this;
        }

        public Builder pathUrl(String pathUrl) {
            this.pathUrl = pathUrl;
            if (!TextUtils.isEmpty(pathUrl)) {
                url = "";
            }
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public CloudNetWorkHttpUrl build() {
            if (TextUtils.isEmpty(url)) {
                url = baseUrl + pathUrl;
            }
            return new CloudNetWorkHttpUrl(this);
        }
    }
}
