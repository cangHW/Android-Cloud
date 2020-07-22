package com.proxy.service.network.cache;

/**
 * @author : cangHX
 * on 2020/07/21  5:23 PM
 */
public enum RequestInfo {

    /**
     * 单例
     */
    INSTANCE;

    /**
     * 毫秒
     */
    private int mRequestTimeout;

    private int mRetryCount = 0;

    public int getRequestTimeout() {
        return mRequestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.mRequestTimeout = requestTimeout;
    }

    public int getRetryCount() {
        return mRetryCount;
    }

    public void setRetryCount(int retryCount) {
        this.mRetryCount = retryCount;
    }
}
