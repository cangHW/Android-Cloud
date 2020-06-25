package com.proxy.service.api.callback;

import com.proxy.service.api.enums.CloudInstallStatusEnum;

/**
 * @author: cangHX
 * on 2020/06/24  17:10
 * <p>
 * 安装回调接口
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
