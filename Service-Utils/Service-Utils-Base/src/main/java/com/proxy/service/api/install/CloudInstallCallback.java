package com.proxy.service.api.install;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 安装回调接口
 *
 * @author: cangHX
 * on 2020/06/24  17:10
 */
public interface CloudInstallCallback {

    /**
     * 安装状态变化
     *
     * @param packageName            : 包名
     * @param cloudInstallStatusEnum : 安装状态
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-24 17:10
     */
    void onStatusChanged(@Nullable String packageName, @NonNull CloudInstallStatusEnum cloudInstallStatusEnum);

}
