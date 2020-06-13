package com.cloud.api.error;

import android.text.TextUtils;

/**
 * @author: cangHX
 * on 2020/06/11  12:57
 * <p>
 * 一些默认异常，如未初始化等
 * 初始化问题，10000 - 20000
 * 数据错误,  20000 - 30000
 * 权限问题,  30000 - 40000
 * 网络问题,  40000 - 50000
 * 未知异常,  50000 -
 */
public enum CloudApiError {

    /**
     * 未初始化错误
     */
    NO_INIT(10000, "Do you init CloudSystem?"),

    /**
     * 数据错误
     */
    DATA_ERROR(20000, "There's some wrong data"),

    /**
     * 数据不存在
     */
    DATA_EMPTY(20001, "The data is empty"),

    /**
     * 获取PackageManager失败
     */
    PACKAGE_MANAGER_ERROR(20100, "The PackageManager is error"),

    /**
     * 获取ActivityManager失败
     */
    ACTIVITY_MANAGER_ERROR(20101, "The ActivityManager is error");

    private final int errorCode;
    private final String errorMsg;

    private String msg;
    private String aboutMsg;

    CloudApiError(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /**
     * 打印日志
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 13:01
     */
    public String build() {
        String info = "errorCode : " + errorCode + ", errorMsg : ";
        if (TextUtils.isEmpty(msg)) {
            info += errorMsg;
        } else {
            info += msg;
        }
        if (!TextUtils.isEmpty(aboutMsg)) {
            info += " \nAbout : " + aboutMsg;
        }
        return info;
    }

    /**
     * 打印日志
     *
     * @param msg : 日志内容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 13:01
     */
    public CloudApiError setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 打印日志
     *
     * @param msg : 日志内容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 13:01
     */
    public CloudApiError append(String msg) {
        this.aboutMsg += " " + msg;
        return this;
    }

    /**
     * 打印日志
     *
     * @param throwable : 异常信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 13:01
     */
    public CloudApiError setMsg(Throwable throwable) {
        this.msg = throwable.getMessage();
        return this;
    }
}
