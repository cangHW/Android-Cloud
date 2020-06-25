package com.proxy.service.api.enums;

import android.content.Intent;

/**
 * @author: cangHX
 * on 2020/06/24  17:06
 * <p>
 * 安装状态枚举
 */
public enum CloudInstallStatusEnum {

    /**
     * 清除数据
     */
    PACKAGE_DATA_CLEARED(Intent.ACTION_PACKAGE_DATA_CLEARED),

    /**
     * 首次启动
     */
    PACKAGE_FIRST_LAUNCH(Intent.ACTION_PACKAGE_FIRST_LAUNCH),

    /**
     * 安装应用
     */
    PACKAGE_ADDED(Intent.ACTION_PACKAGE_ADDED),

    /**
     * 删除应用
     */
    PACKAGE_REMOVED(Intent.ACTION_PACKAGE_REMOVED),

    /**
     * 完全删除应用
     */
    PACKAGE_FULLY_REMOVED(Intent.ACTION_PACKAGE_FULLY_REMOVED),

    /**
     * 更新应用
     */
    PACKAGE_REPLACED(Intent.ACTION_PACKAGE_REPLACED),

    /**
     * 重新启动
     */
    PACKAGE_RESTARTED(Intent.ACTION_PACKAGE_RESTARTED),

    /**
     * 自身应用更新
     */
    MY_PACKAGE_REPLACED(Intent.ACTION_MY_PACKAGE_REPLACED);

    String value;

    CloudInstallStatusEnum(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
