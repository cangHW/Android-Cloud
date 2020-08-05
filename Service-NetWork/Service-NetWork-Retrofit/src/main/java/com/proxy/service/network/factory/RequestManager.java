package com.proxy.service.network.factory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.method.CloudNetWorkRequest;
import com.proxy.service.api.callback.request.NetWorkCallback;
import com.proxy.service.api.callback.request.RequestCallback;
import com.proxy.service.api.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author : cangHX
 * on 2020/07/31  3:19 PM
 */
public class RequestManager implements NetWorkCallback {

    private int mRetryCount = -1;
    private boolean isHasCookie = true;

    private Logger mLogger = Logger.create("RequestManager");

    private RequestManager(Builder builder) {
        this.mRetryCount = builder.mRetryCount;
        this.isHasCookie = builder.isHasCookie;
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
        mLogger.error("setRequestBody");
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
        return null;
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
    public void enqueue(@NonNull RequestCallback callback) {
        mLogger.error("enqueue");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name","请求成功");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        callback.onResponse(200, jsonObject.toString());
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

    }

    public static class Builder {

        private int mRetryCount = -1;
        private boolean isHasCookie = true;

        public Builder setRetryCount(int retryCount) {
            this.mRetryCount = retryCount;
            return this;
        }

        public Builder setHasCookie(boolean hasCookie) {
            this.isHasCookie = hasCookie;
            return this;
        }

        public RequestManager build() {
            return new RequestManager(this);
        }
    }
}
