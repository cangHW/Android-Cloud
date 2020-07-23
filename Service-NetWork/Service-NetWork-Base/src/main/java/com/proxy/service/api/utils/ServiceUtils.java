package com.proxy.service.api.utils;

import com.proxy.service.api.error.CloudApiError;

/**
 * @author : cangHX
 * on 2020/7/23 9:09 PM
 */
public class ServiceUtils {

    /**
     * 检测类是否是合格的接口类，是否允许发起网络请求
     *
     * @param service : 即将被检测的类
     * @return 检测结果，true 符合条件，false 不符合
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/23 9:09 PM
     */
    public static <T> boolean checkServiceInterface(Class<T> service) {
        if (!service.isInterface()) {
            Logger.Debug(CloudApiError.DATA_TYPE_ERROR.setMsg("API declarations must be interfaces. with " + service.getCanonicalName()).build());
            return false;
        }
        if (service.getInterfaces().length > 0) {
            Logger.Debug(CloudApiError.DATA_TYPE_ERROR.setMsg("API interfaces must not extend other interfaces. with " + service.getCanonicalName()).build());
            return false;
        }
        return true;
    }

}
