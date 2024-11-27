package com.proxy.service.network.factory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.request.method.CloudNetWorkRequest;
import com.proxy.service.api.callback.request.NetWorkCallback;
import com.proxy.service.api.callback.request.RequestCallback;
import com.proxy.service.api.services.CloudUtilsNetWorkService;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.log.Logger;
import com.proxy.service.network.utils.RequestUtils;

import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author : cangHX
 * on 2020/07/31  3:19 PM
 */
public final class RequestManager implements NetWorkCallback {

    private static final Logger mLogger = Logger.create("RequestManager");

    private final int mRequestCount;
    private final boolean isUseCookie;

    private CloudNetWorkRequest mRequestBody;
    private final CloudUtilsTaskService mTaskService;
    private final CloudUtilsNetWorkService mNetWorkService;

    private RequestManager(Builder builder) {
        this.mRequestCount = builder.mRequestCount;
        this.isUseCookie = builder.isUseCookie;
        this.mTaskService = CloudSystem.getService(CloudUtilsTaskService.class);
        this.mNetWorkService = CloudSystem.getService(CloudUtilsNetWorkService.class);
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 设置请求体
     *
     * @param requestBody : 请求体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 5:11 PM
     */
    @Override
    public void setRequestBody(@NonNull CloudNetWorkRequest requestBody) {
        mLogger.info("Request body received");
        this.mRequestBody = requestBody;
    }

    /**
     * 同步请求
     *
     * @return 获取到的返回值 jsonString
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 10:55 PM
     */
    @Nullable
    @Override
    public String execute() {
        final String[] value = {null};
        mLogger.info("Request start. with : execute");

        if (mNetWorkService == null) {
            Logger.Error("Please check whether to use \"exclude\" to remove partial dependencies");
            return null;
        }
        if (!mNetWorkService.isConnected()) {
            Logger.Debug(CloudApiError.NET_WORK_EMPTY.build());
            return null;
        }

        if (mTaskService == null) {
            Logger.Error("Please check whether to use \"exclude\" to remove partial dependencies");
            return null;
        }

        final String url = RequestUtils.getUrl(mRequestBody);
        final Map<String, String> headers = RequestUtils.getHeaders(mRequestBody, isUseCookie);
        final Map<String, String> params = RequestUtils.getParams(mRequestBody);

        final AtomicInteger count = new AtomicInteger(0);
        mTaskService.continueWhile(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return count.incrementAndGet() <= mRequestCount;
            }
        }, new Task<Object>() {
            @Override
            public Object call() {
                mLogger.info("Request start. count : " + count.get() + ", max count : " + mRequestCount);

                Call<String> call = RequestUtils.getCall(mRequestBody.httpMethod(), url, headers, params);
                if (call == null) {
                    Logger.Error(CloudApiError.UNKNOWN_ERROR.build());
                    return null;
                }
                try {
                    Response<String> response = call.execute();
                    if (response.isSuccessful()) {
                        count.set(mRequestCount + 1);
                        value[0] = response.body();
                        return "";
                    }
                } catch (Throwable e) {
                    mLogger.debug(e);
                    boolean isSocketTimeout = e instanceof SocketTimeoutException;
                    if (!isSocketTimeout) {
                        count.set(mRequestCount + 1);
                    }
                }
                return "";
            }
        });
        return value[0];
    }

    /**
     * 异步请求
     *
     * @param callback : 请求回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 10:55 PM
     */
    @Override
    public void enqueue(@NonNull final RequestCallback callback) {
        mLogger.info("Request start. with : enqueue");

        if (mNetWorkService == null) {
            Logger.Debug("Please check whether to use \"exclude\" to remove partial dependencies");
            callback.onFailure(new IllegalAccessException("Please check whether to use \"exclude\" to remove partial dependencies"));
            return;
        }
        if (!mNetWorkService.isConnected()) {
            Logger.Debug(CloudApiError.NET_WORK_EMPTY.build());
            callback.onFailure(new IllegalAccessException("There is no network"));
            return;
        }

        final String url = RequestUtils.getUrl(mRequestBody);
        final Map<String, String> headers = RequestUtils.getHeaders(mRequestBody, isUseCookie);
        final Map<String, String> params = RequestUtils.getParams(mRequestBody);

        final AtomicInteger count = new AtomicInteger(0);

        request(count, url, headers, params, callback);
    }

    private void request(final AtomicInteger count, final String url, final Map<String, String> headers, final Map<String, String> params, final RequestCallback callback) {
        mLogger.info("Request start. count : " + count.get() + ", max count : " + mRequestCount);
        final Call<String> call = RequestUtils.getCall(mRequestBody.httpMethod(), url, headers, params);
        if (call == null) {
            Logger.Error(CloudApiError.UNKNOWN_ERROR.build());
            return;
        }
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(response.code(), response.body());
                } else {
                    callback.onFailure(new IllegalAccessException("request error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                boolean isSocketTimeout = t instanceof SocketTimeoutException;
                if (isSocketTimeout && count.getAndIncrement() < mRequestCount) {
                    request(count, url, headers, params, callback);
                    return;
                }
                callback.onFailure(t);
            }
        });
    }

    /**
     * 取消请求
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 10:59 PM
     */
    @Override
    public void cancel() {
        if (mTaskService != null) {
            mTaskService.cancel();
        }
    }

    public static class Builder {

        private int mRequestCount = 1;
        private boolean isUseCookie = true;

        public Builder setRetryCount(int retryCount) {
            if (retryCount > 0) {
                this.mRequestCount += retryCount;
            }
            return this;
        }

        public Builder isUseCookie(boolean isUseCookie) {
            this.isUseCookie = isUseCookie;
            return this;
        }

        public RequestManager build() {
            return new RequestManager(this);
        }
    }
}
