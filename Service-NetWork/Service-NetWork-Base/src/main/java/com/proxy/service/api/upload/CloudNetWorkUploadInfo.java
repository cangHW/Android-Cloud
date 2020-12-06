package com.proxy.service.api.upload;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.proxy.service.api.callback.upload.CloudUploadCallback;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/11/09  9:31 PM
 */
public final class CloudNetWorkUploadInfo {

    private final String uploadUrl;

    private final List<String> paths = new ArrayList<>();
    private final List<OutputStream> streams = new ArrayList<>();
    private final HashMap<String, String> params = new HashMap<>();
    private final String contentType;

    private final CloudUploadCallback uploadCallback;

    private CloudNetWorkUploadInfo(Builder builder) {
        this.uploadUrl = builder.uploadUrl;
        this.paths.clear();
        this.paths.addAll(builder.paths);
        this.streams.clear();
        this.streams.addAll(builder.streams);
        this.params.clear();
        this.params.putAll(builder.params);
        this.contentType = builder.contentType;
        this.uploadCallback = builder.uploadCallback;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public List<String> getPaths() {
        return paths;
    }

    public List<OutputStream> getStreams() {
        return streams;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public String getContentType() {
        return contentType;
    }

    public CloudUploadCallback getUploadCallback() {
        return uploadCallback;
    }

    public Builder build() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String uploadUrl;

        private final List<String> paths = new ArrayList<>();
        private final List<OutputStream> streams = new ArrayList<>();
        private final HashMap<String, String> params = new HashMap<>();
        private String contentType = "";

        private CloudUploadCallback uploadCallback;

        private Builder() {
        }

        private Builder(CloudNetWorkUploadInfo info) {
            this.uploadUrl = info.getUploadUrl();
            this.paths.clear();
            this.paths.addAll(info.getPaths());
            this.streams.clear();
            this.streams.addAll(info.getStreams());
            this.params.clear();
            this.params.putAll(info.getParams());
            this.contentType = info.getContentType();
            this.uploadCallback = info.getUploadCallback();
        }

        public Builder setUploadUrl(@NonNull String uploadUrl) {
            this.uploadUrl = uploadUrl;
            return this;
        }

        public Builder addFilePath(@NonNull String path) {
            if (!TextUtils.isEmpty(path)) {
                this.paths.add(path);
            }
            return this;
        }

        public Builder addStream(OutputStream stream) {
            if (stream != null) {
                this.streams.add(stream);
            }
            return this;
        }

        public Builder addParam(@NonNull String key, @NonNull String value) {
            if (!TextUtils.isEmpty(key)) {
                if (TextUtils.isEmpty(value)) {
                    value = "";
                }
                params.put(key, value);
            }
            return this;
        }

        public Builder setContentType(@NonNull String contentType) {
            if (!TextUtils.isEmpty(contentType)) {
                this.contentType = contentType;
            }
            return this;
        }

        public Builder setUploadCallback(@NonNull CloudUploadCallback uploadCallback) {
            this.uploadCallback = uploadCallback;
            return this;
        }

        public CloudNetWorkUploadInfo build() {
            return new CloudNetWorkUploadInfo(this);
        }
    }

}
