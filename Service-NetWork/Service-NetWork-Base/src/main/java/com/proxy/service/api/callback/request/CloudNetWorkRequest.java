package com.proxy.service.api.callback.request;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author : cangHX
 * on 2020/07/31  3:36 PM
 */
public final class CloudNetWorkRequest {

    private final CloudNetWorkHttpUrl httpUrl;
    private final CloudNetWorkHeaders headers;
    private final CloudNetWorkParameter parameter;
    private final Method method;
    private final String httpMethod;
    private final Map<Object, Object> tags;

    private CloudNetWorkRequest(Builder builder) {
        this.httpUrl = builder.httpUrl;
        this.headers = builder.headers;
        this.parameter = builder.parameter;
        this.method = builder.method;
        this.httpMethod = builder.httpMethod;
        this.tags = builder.tags;
    }

    public CloudNetWorkHttpUrl httpUrl() {
        return httpUrl;
    }

    public CloudNetWorkHeaders headers() {
        return headers;
    }

    public CloudNetWorkParameter parameter() {
        return parameter;
    }

    public <T extends Annotation> T annotation(Class<T> annotationClass) {
        return method.getAnnotation(annotationClass);
    }

    public Object getTag() {
        return getTag(Object.class);
    }

    public Object getTag(Object key) {
        return tags.get(key);
    }

    public Object putTag(Object key, Object value) {
        return tags.put(key, value);
    }

    public Object removeTag(Object key) {
        return tags.remove(key);
    }

    public Map<Object, Object> tags() {
        return tags;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private CloudNetWorkHttpUrl httpUrl;
        private CloudNetWorkHeaders headers;
        private CloudNetWorkParameter parameter;
        private Method method;
        private String httpMethod;
        private Map<Object, Object> tags;

        public Builder(CloudNetWorkHttpUrl httpUrl, CloudNetWorkHeaders headers, CloudNetWorkParameter parameter, Method method, String httpMethod, Map<Object, Object> tags) {
            this.httpUrl = httpUrl;
            this.headers = headers;
            this.parameter = parameter;
            this.method = method;
            this.httpMethod = httpMethod;
            this.tags = tags;
        }

        public Builder(CloudNetWorkRequest request) {
            this.httpUrl = request.httpUrl;
            this.headers = request.headers;
            this.parameter = request.parameter;
            this.method = request.method;
            this.httpMethod = request.httpMethod;
            this.tags = request.tags;
        }

        public Builder setHttpUrl(CloudNetWorkHttpUrl httpUrl) {
            this.httpUrl = httpUrl;
            return this;
        }

        public Builder setHeaders(CloudNetWorkHeaders headers) {
            this.headers = headers;
            return this;
        }

        public Builder setParameter(CloudNetWorkParameter parameter) {
            this.parameter = parameter;
            return this;
        }

        public Builder setTags(Map<Object, Object> tags) {
            this.tags = tags;
            return this;
        }

        public CloudNetWorkRequest build() {
            return new CloudNetWorkRequest(this);
        }
    }
}
