package com.proxy.service.utils.permission;

/**
 * @author : cangHX
 * on 2021/04/11  6:22 PM
 */
public interface PermissionCallback {
    /**
     * 允许
     *
     * @param permissions : 允许的权限列表
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 2:53 PM
     */
    void onGranted(String[] permissions);

    /**
     * 禁止
     *
     * @param permissions : 禁止的权限列表
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 2:54 PM
     */
    void onDenied(String[] permissions);

    /**
     * 不再询问
     *
     * @param permissions : 不再询问的权限列表
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 5:08 PM
     */
    void onRationale(String[] permissions);
}
