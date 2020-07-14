package com.proxy.service.api.service;

import android.content.Context;

import com.proxy.service.api.multidex.ClassUtils;
import com.proxy.service.api.service.cache.OtherCache;
import com.proxy.service.api.service.cache.ServiceCache;
import com.proxy.service.base.AbstractServiceCache;
import com.proxy.service.consts.ClassConstants;

import java.util.Set;

/**
 * @author: cangHX
 * on 2020/06/30  17:36
 */
public enum DataManager {

    /**
     * 单例对象
     */
    INSTANCE;

    /**
     * 注册所有的服务
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 17:25
     */
    public synchronized void registerAllServices(Context context) {
        findAllServices(context);
    }

    /**
     * 通过辅助文件，加载所有可以加载的服务
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 18:01
     */
    private void findAllServices(Context context) {
        try {
            Set<String> stringSet = ClassUtils.getFileNameByPackageName(context, ClassConstants.PACKAGE_SERVICES_CACHE);
            for (String classPath : stringSet) {
                if (!classPath.startsWith(ClassConstants.PACKAGE_SERVICES_CACHE + "." + ClassConstants.CLASS_PREFIX)) {
                    continue;
                }

                AbstractServiceCache cache = null;
                try {
                    cache = (AbstractServiceCache) Class.forName(classPath).getConstructor().newInstance();
                } catch (Throwable ignored) {
                }

                if (cache == null) {
                    continue;
                }

                try {
                    ServiceCache.addAll(cache.getServices());
                } catch (Throwable ignored) {
                }

                try {
                    OtherCache.addAll(cache.getOthers());
                } catch (Throwable ignored) {
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
