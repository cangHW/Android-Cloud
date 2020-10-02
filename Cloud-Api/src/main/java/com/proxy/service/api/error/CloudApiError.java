package com.proxy.service.api.error;

import android.text.TextUtils;

/**
 * 一些默认异常，如未初始化等
 * 初始化问题，10000 - 20000
 * 数据错误,  20000 - 30000
 * 权限问题,  30000 - 40000
 * 网络问题,  40000 - 50000
 * 未知异常,  100000 -
 *
 * @author: cangHX
 * on 2020/06/11  12:57
 */
public enum CloudApiError {

    /*****************  初始化问题  *********************/
    /**
     * 未初始化错误
     */
    INIT_EMPTY(10000, "Do you init CloudSystem?"),

    /**
     * 只能初始化一次
     */
    INIT_ONCE(10001, "It can only be init once"),

    /****************  数据错误  ******************/
    /**
     * 数据错误
     */
    DATA_ERROR(20000, "There's some wrong data"),

    /**
     * 数据不存在
     */
    DATA_EMPTY(20001, "The data is empty"),

    /**
     * 数据类型错误
     */
    DATA_TYPE_ERROR(20002, "The data type is empty"),

    /**
     * 数据重复
     */
    DATA_DUPLICATION(20003, "The data is duplication"),

    /**
     * 数据过多
     */
    DATA_TO_MORE(20004, "The data is to more"),

    /**
     * 获取PackageManager失败
     */
    PACKAGE_MANAGER_ERROR(20100, "The PackageManager is error"),

    /**
     * 获取ActivityManager失败
     */
    ACTIVITY_MANAGER_ERROR(20101, "The ActivityManager is error"),

    /**
     * 时间错误
     */
    TIME_ERROR(20200, "The time is error"),

    /**
     * 数组越界
     */
    INDEX_OUT_OF_BOUNDS(20300, "Index out of bounds"),

    /******************  权限问题  *************************/
    /**
     * 安装过程存在安全隐患
     */
    INSTALL_SECURITY_PATH_ERROR(30000, "Lack of security path"),

    /**
     * 权限被拒绝
     */
    PERMISSION_DENIED(30001, "Please check permissions"),

    /******************  网络问题  *************************/

    NET_WORK_EMPTY(40000, "There is no network"),

    /******************  未知异常  *************************/
    UNKNOWN_ERROR(100000, "Unknown error");


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
    public CloudApiError setAbout(String msg) {
        this.aboutMsg = " " + msg;
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
