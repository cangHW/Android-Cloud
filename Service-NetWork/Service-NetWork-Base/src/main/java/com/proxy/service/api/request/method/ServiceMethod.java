package com.proxy.service.api.request.method;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.proxy.service.api.request.annotations.CloudNetWorkBaseUrl;
import com.proxy.service.api.request.annotations.CloudNetWorkBaseUrlId;
import com.proxy.service.api.request.annotations.CloudNetWorkField;
import com.proxy.service.api.request.annotations.CloudNetWorkFormUrlEncoded;
import com.proxy.service.api.request.annotations.CloudNetWorkGet;
import com.proxy.service.api.request.annotations.CloudNetWorkHeader;
import com.proxy.service.api.request.annotations.CloudNetWorkHeaders;
import com.proxy.service.api.request.annotations.CloudNetWorkPost;
import com.proxy.service.api.request.annotations.CloudNetWorkTag;
import com.proxy.service.api.request.annotations.CloudNetWorkUrl;
import com.proxy.service.api.request.annotations.HttpMethod;
import com.proxy.service.api.request.cache.BaseUrlCache;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.log.Logger;
import com.proxy.service.api.log.ServiceUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author : cangHX
 * on 2020/7/24 8:57 PM
 */
public class ServiceMethod {

    private final boolean isRequestBodyError;

    private final Method method;

    private final String baseUrl;
    private final String pathUrl;
    private final String url;
    @HttpMethod
    private final String httpMethod;
    private final boolean isFormEncoded;
    private final Map<String, String> headersMapper;
    private final ServiceParameterHandler<?>[] serviceParameterHandlers;
    private final ServiceReturnHandler serviceReturnHandler;

    private final String tag;

    private ServiceMethod(Builder builder) {
        this.isRequestBodyError = builder.isRequestBodyError;
        this.method = builder.method;
        this.baseUrl = builder.baseUrl;
        this.pathUrl = builder.urlPath;
        this.url = builder.url;
        this.httpMethod = builder.httpMethod;
        this.isFormEncoded = builder.isFormEncoded;
        this.headersMapper = builder.headersMapper;
        this.serviceParameterHandlers = builder.serviceParameterHandlers;
        this.serviceReturnHandler = builder.serviceReturnHandler;
        this.tag = builder.tag;
    }

    public boolean isRequestBodyError() {
        return isRequestBodyError;
    }

    @Nullable
    public ServiceReturnHandler getServiceReturnHandler() {
        return serviceReturnHandler;
    }

    @SuppressWarnings("unchecked")
    public CloudNetWorkRequest request(Object[] args) {
        CloudNetWorkHttpUrl.Builder httpUrlBuilder = new CloudNetWorkHttpUrl.Builder();
        httpUrlBuilder.baseUrl(this.baseUrl);
        httpUrlBuilder.pathUrl(this.pathUrl);
        httpUrlBuilder.url(this.url);

        com.proxy.service.api.request.method.CloudNetWorkHeaders.Builder headersBuilder = new com.proxy.service.api.request.method.CloudNetWorkHeaders.Builder();
        for (Map.Entry<String, String> entry : headersMapper.entrySet()) {
            headersBuilder.addHeader(entry.getKey(), entry.getValue());
        }

        CloudNetWorkParameter.Builder parameterBuilder = new CloudNetWorkParameter.Builder();
        int count = args.length;
        for (int i = 0; i < count; i++) {
            Object object = args[i];
            ServiceParameterHandler<Object> parameterHandler = (ServiceParameterHandler<Object>) serviceParameterHandlers[i];
            parameterHandler.apply(parameterBuilder.getMap(), object);
        }

        Map<Object, Object> tags = new HashMap<>();
        tags.put(Object.class, this.tag);

        return new CloudNetWorkRequest.Builder(httpUrlBuilder.build(), headersBuilder.build(), parameterBuilder.build(), this.method, this.httpMethod, this.isFormEncoded, tags).build();
    }

    public static ServiceMethod parseAnnotations(Method method) {
        return new Builder(method).build();
    }

    private static final class Builder {
        private static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
        private static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{(" + PARAM + ")\\}");

        private final Method method;
        private final Annotation[] methodAnnotations;
        private final Annotation[][] parameterAnnotationsArray;
        private final Type[] parameterTypes;

        private boolean isRequestBodyError = false;

        private String baseUrl;
        private String urlPath;
        private String url;
        @HttpMethod
        private String httpMethod;
        private boolean isFormEncoded;
        private Map<String, String> headersMapper = new HashMap<>();
        private ServiceParameterHandler<?>[] serviceParameterHandlers;
        private ServiceReturnHandler serviceReturnHandler;

        private String tag;

        public Builder(Method method) {
            this.method = method;
            this.methodAnnotations = method.getAnnotations();
            this.parameterTypes = method.getGenericParameterTypes();
            this.parameterAnnotationsArray = method.getParameterAnnotations();

            this.baseUrl = BaseUrlCache.getBaseUrl();
        }

        public ServiceMethod build() {
            headersMapper.clear();

            for (Annotation annotation : methodAnnotations) {
                parseMethodAnnotation(annotation);
            }
            if (TextUtils.isEmpty(httpMethod)) {
                Logger.Error(CloudApiError.DATA_EMPTY.setMsg("Network request mode missing. with @CloudNetWorkGet、@CloudNetWorkPost...").build());
                isRequestBodyError = true;
            }

            int parameterCount = parameterAnnotationsArray.length;
            serviceParameterHandlers = new ServiceParameterHandler<?>[parameterCount];
            for (int i = 0; i < parameterCount; i++) {
                serviceParameterHandlers[i] = parseParameter(i, parameterTypes[i], parameterAnnotationsArray[i]);
            }

            Type returnType = this.method.getGenericReturnType();
            if (ServiceUtils.checkReturnType(returnType)) {
                this.serviceReturnHandler = new ServiceReturnHandler(returnType);
            } else {
                isRequestBodyError = true;
            }

            //todo url 中 path 部分动态替换

            return new ServiceMethod(this);
        }

        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof CloudNetWorkGet) {
                parseHttpMethodAndPath(HttpMethod.GET, ((CloudNetWorkGet) annotation).value());
            } else if (annotation instanceof CloudNetWorkPost) {
                parseHttpMethodAndPath(HttpMethod.POST, ((CloudNetWorkPost) annotation).value());
            } else if (annotation instanceof CloudNetWorkBaseUrl) {
                parseBaseUrl(null, ((CloudNetWorkBaseUrl) annotation).value());
            } else if (annotation instanceof CloudNetWorkBaseUrlId) {
                parseBaseUrl(((CloudNetWorkBaseUrlId) annotation).value(), null);
            } else if (annotation instanceof CloudNetWorkHeader) {
                parseHeader(new CloudNetWorkHeader[]{(CloudNetWorkHeader) annotation});
            } else if (annotation instanceof CloudNetWorkHeaders) {
                parseHeader(((CloudNetWorkHeaders) annotation).value());
            } else if (annotation instanceof CloudNetWorkUrl) {
                parseUrl(((CloudNetWorkUrl) annotation).value());
            } else if (annotation instanceof CloudNetWorkFormUrlEncoded) {
                parseUrlEncoded();
            } else if (annotation instanceof CloudNetWorkTag) {
                parseTag(((CloudNetWorkTag) annotation).value());
            } else {
                Logger.Warning("unknown annotation with " + annotation.toString());
            }
        }

        private void parseHttpMethodAndPath(String httpMethod, String urlPath) {
            this.httpMethod = httpMethod;
            //todo 动态替换 url 一部分

            this.urlPath = urlPath;
        }

        private void parseBaseUrl(String urlId, String baseUrl) {
            if (!TextUtils.isEmpty(urlId)) {
                this.baseUrl = BaseUrlCache.getBaseUrl(urlId);
            } else if (!TextUtils.isEmpty(baseUrl)) {
                this.baseUrl = baseUrl;
            } else {
                this.baseUrl = BaseUrlCache.getBaseUrl();
            }
        }

        private void parseHeader(CloudNetWorkHeader[] headers) {
            if (headers == null) {
                return;
            }
            for (CloudNetWorkHeader header : headers) {
                if (header == null) {
                    continue;
                }
                String key = header.key();
                String value = header.value();
                headersMapper.put(key, value);
            }
        }

        private void parseUrl(String url) {
            this.url = url;
        }

        private void parseTag(String tag) {
            this.tag = tag;
        }

        private void parseUrlEncoded() {
            this.isFormEncoded = true;
        }

        private ServiceParameterHandler<?> parseParameter(int position, Type parameterType, Annotation[] annotations) {
            if (annotations == null) {
                return null;
            }
            ServiceParameterHandler<?> parameter = null;
            for (Annotation annotation : annotations) {
                if (parameter != null) {
                    Logger.Debug(CloudApiError.DATA_DUPLICATION.setMsg("Multiple CloudNetWork annotations found, only one allowed. with parameter : " + (position + 1)).build());
                    continue;
                }
                ServiceParameterHandler<?> result = parseParameterAnnotation(position, parameterType, annotation);
                if (result == null) {
                    continue;
                }
                parameter = result;
            }

            if (parameter == null) {
                Logger.Error(CloudApiError.DATA_EMPTY.setMsg("No CloudNetWork annotation found. with parameter : " + (position + 1)).build());
            }

            return parameter;
        }

        private ServiceParameterHandler<?> parseParameterAnnotation(int position, Type parameterType, Annotation annotation) {
            if (annotation == null) {
                return null;
            }
            if (annotation instanceof CloudNetWorkField) {
                if (ServiceUtils.isBasicTypes(parameterType)) {
                    return new ServiceParameterHandler.CloudNetWorkFieldHandler(parameterType, (CloudNetWorkField) annotation);
                }
            }
            Logger.Error(CloudApiError.DATA_ERROR.setMsg("The type of annotation is error. with parameter : " + (position + 1) + ", and annotation : " + annotation.toString()).build());
            return null;
        }
    }

}
