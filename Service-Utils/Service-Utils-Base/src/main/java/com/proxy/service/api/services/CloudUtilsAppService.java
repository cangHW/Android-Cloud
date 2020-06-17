package com.proxy.service.api.services;

import com.proxy.service.base.BaseService;

/**
 * @author: cangHX
 * on 2020/06/10  19:00
 * <p>
 * app相关
 */
public interface CloudUtilsAppService extends BaseService {

    /**
     * 获取当前app包名
     *
     * @return 包名
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-10 19:04
     */
    String getPackageName();

    /**
     * 获取当前app版本code
     *
     * @return 版本code
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 09:52
     */
    int getVersionCode();

    /**
     * 获取当前app版本name
     *
     * @return 版本name
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 09:53
     */
    String getVersionName();

    /**
     * 判断app是否处于后台
     *
     * @return true 后台，false 前台
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:18
     */
    boolean isBackground();
}
