package com.proxy.service.api.permission;

import com.proxy.service.api.action.Action;

/**
 * @author : cangHX
 * on 2021/04/11  6:03 PM
 */
public interface IPermissionManager {

    /**
     * 添加要申请的权限
     *
     * @param permission : 要申请的权限
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 6:05 PM
     */
    IPermissionManager addPermission(String permission);

    /**
     * 设置允许权限回调
     *
     * @param action : 回调对象
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 6:10 PM
     */
    IPermissionManager onGranted(Action<String[]> action);

    /**
     * 设置拒绝权限回调
     *
     * @param action : 回调对象
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 6:10 PM
     */
    IPermissionManager onDenied(Action<String[]> action);

    /**
     * 设置拒绝并不再提示权限回调
     *
     * @param action : 回调对象
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 6:10 PM
     */
    IPermissionManager onRationale(Action<String[]> action);

    /**
     * 开始请求
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 6:12 PM
     */
    void request();
}
