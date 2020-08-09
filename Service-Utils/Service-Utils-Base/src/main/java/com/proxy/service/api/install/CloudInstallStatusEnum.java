package com.proxy.service.api.install;

import android.content.Intent;

/**
 * 安装状态枚举
 *
 * @author: cangHX
 * on 2020/06/24  17:06
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

    final String value;

    CloudInstallStatusEnum(String value) {
        this.value = value;
    }

    public static CloudInstallStatusEnum of(String action) {
        if (action.equals(PACKAGE_DATA_CLEARED.value)) {
            return PACKAGE_DATA_CLEARED;
        } else if (action.equals(PACKAGE_FIRST_LAUNCH.value)) {
            return PACKAGE_FIRST_LAUNCH;
        } else if (action.equals(PACKAGE_ADDED.value)) {
            return PACKAGE_ADDED;
        } else if (action.equals(PACKAGE_REMOVED.value)) {
            return PACKAGE_REMOVED;
        } else if (action.equals(PACKAGE_FULLY_REMOVED.value)) {
            return PACKAGE_FULLY_REMOVED;
        } else if (action.equals(PACKAGE_REPLACED.value)) {
            return PACKAGE_REPLACED;
        } else if (action.equals(PACKAGE_RESTARTED.value)) {
            return PACKAGE_RESTARTED;
        } else if (action.equals(MY_PACKAGE_REPLACED.value)) {
            return MY_PACKAGE_REPLACED;
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
