package com.proxy.service.api.net;

/**
 * @author : cangHX
 * on 2020/08/28  6:16 PM
 */
public enum CloudNetWorkType {

    /**
     * 获取网络类型失败
     */
    ERROR("error"),

    /**
     * 未知
     */
    UNKNOWN("unknown"),

    /**
     * wifi
     */
    WIFI("wifi"),

    /**
     * mobile
     * 无法获取具体移动类型
     */
    MOBILE("mobile"),

    /**
     * 2g
     */
    MOBILE_2G("2g"),

    /**
     * 3g
     */
    MOBILE_3G("3g"),

    /**
     * 4g
     */
    MOBILE_4G("4g"),

    /**
     * 5g
     */
    MOBILE_5G("5g");

    private final String type;

    CloudNetWorkType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
