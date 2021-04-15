package com.proxy.service.utils.permission;

import com.proxy.service.utils.info.UtilsPermissionServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : cangHX
 * on 2021/04/11  3:22 PM
 */
public interface IPermissionFragment {

    AtomicInteger REQUEST_CODE = new AtomicInteger(0);

    UtilsPermissionServiceImpl SERVICE = new UtilsPermissionServiceImpl();

    class PermissionInfo {
        PermissionCallback callback;
        ArrayList<String> deniedPermissions;
        ArrayList<String> grantedPermissions;
        ArrayList<String> rationalePermissions;
    }

    /**
     * 添加准备申请的权限信息
     *
     * @param callback    : 权限回调
     * @param permissions : 权限信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 3:34 PM
     */
    void addPermissionInfo(PermissionCallback callback, List<String> permissions);

    /**
     * 开始申请权限
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/4/11 3:35 PM
     */
    void request();
}
