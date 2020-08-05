package com.proxy.service.network.cache;

/**
 * @author : cangHX
 * on 2020/07/21  9:23 PM
 */
public final class RequestInfo {

    /**
     * 请求超时，毫秒
     */
    private static int mRequestTimeout = -1;

    /**
     * 重试次数
     */
    private static int mRetryCount = 0;

    /**
     * 读取超时
     */
    private static int mReadTimeout = -1;

    /**
     * 写入超时
     */
    private static int mWriteTimeout = -1;

    /**
     * 连接超时
     */
    private static int mConnectTimeout = -1;

    /**
     * 请求超时，毫秒
     */
    public static int getRequestTimeout() {
        int timeOut = -1;
        if (mRequestTimeout == -1) {
            timeOut = mReadTimeout + mWriteTimeout + mConnectTimeout;
        }
        if (timeOut <= 0) {
            timeOut = -1;
        }
        return timeOut;
    }

    public static void setRequestTimeout(int requestTimeout) {
        mRequestTimeout = requestTimeout;
    }

    public static int getRetryCount() {
        return mRetryCount;
    }

    public static void setRetryCount(int retryCount) {
        mRetryCount = retryCount;
    }

    public static void setReadTimeout(int readTimeout) {
        mReadTimeout = readTimeout;
    }

    public static void setWriteTimeout(int writeTimeout) {
        mWriteTimeout = writeTimeout;
    }

    public static void setConnectTimeout(int connectTimeout) {
        mConnectTimeout = connectTimeout;
    }
}
