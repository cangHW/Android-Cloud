package com.proxy.service.api.services;

import com.proxy.service.base.BaseService;

/**
 * 权限相关
 *
 * @author : cangHX
 * on 2020/08/31  9:16 PM
 */
public interface CloudUtilsPermissionService extends BaseService {

    /**
     * 是否具有对应权限
     *
     * @param permission : 权限名称
     * @return true 有，false 没有
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/31 9:19 PM
     */
    boolean isPermissionGranted(String permission);

    /**
     * 自动获取对应权限
     *
     * @param permission : 权限名称
     * @return true 有，false 没有
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/31 9:20 PM
     */
    boolean selfPermissionGranted(String permission);

}
