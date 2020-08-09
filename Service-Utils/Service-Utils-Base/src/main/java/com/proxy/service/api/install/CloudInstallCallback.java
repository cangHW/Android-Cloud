package com.proxy.service.api.install;

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
     * @param cloudInstallStatusEnum : 安装状态
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-24 17:10
     */
    void onStatusChanged(CloudInstallStatusEnum cloudInstallStatusEnum);

}
