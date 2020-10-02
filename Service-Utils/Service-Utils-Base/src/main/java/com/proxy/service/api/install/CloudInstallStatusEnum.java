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
     * 安装应用
     */
    PACKAGE_ADDED(Intent.ACTION_PACKAGE_ADDED),

    /**
     * 删除应用
     */
    PACKAGE_REMOVED(Intent.ACTION_PACKAGE_REMOVED);

    final String value;

    CloudInstallStatusEnum(String value) {
        this.value = value;
    }

    public static CloudInstallStatusEnum of(String action) {
        if (action.equals(PACKAGE_ADDED.value)) {
            return PACKAGE_ADDED;
        } else if (action.equals(PACKAGE_REMOVED.value)) {
            return PACKAGE_REMOVED;
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
