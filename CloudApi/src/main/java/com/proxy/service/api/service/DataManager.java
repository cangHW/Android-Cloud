package com.proxy.service.api.service;

import android.content.Context;

import com.proxy.service.api.multidex.ClassUtils;
import com.proxy.service.api.plugin.DataByPlugin;
import com.proxy.service.api.service.cache.ServiceCache;
import com.proxy.service.api.log.Logger;
import com.proxy.service.base.AbstractServiceCache;
import com.proxy.service.consts.ClassConstants;

import java.util.ArrayList;
import java.util.List;
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
        if (findAllFromCache()) {
            return;
        }
        findAllFromDex(context);
    }

    private boolean findAllFromCache() {
        try {
            List<String> classPaths = DataByPlugin.getInstance().getClasses(new ArrayList<>());
            if (classPaths.size() == 0) {
                return false;
            }
            for (String classPath : classPaths) {
                checkClass(classPath);
            }
            return true;
        } catch (Throwable throwable) {
            Logger.INSTANCE.d(throwable);
        }
        return false;
    }

    private void findAllFromDex(Context context) {
        Logger.INSTANCE.w("DexFile is about to be removed, so please update the microservice architecture version as soon as possible.");
        try {
            Set<String> stringSet = ClassUtils.getFileNameByPackageName(context, ClassConstants.PACKAGE_SERVICES_CACHE);
            for (String classPath : stringSet) {
                checkClass(classPath);
            }
        } catch (Throwable throwable) {
            Logger.INSTANCE.d(throwable);
        }
    }

    private void checkClass(String classPath){
        if (!classPath.startsWith(ClassConstants.PACKAGE_SERVICES_CACHE + "." + ClassConstants.CLASS_PREFIX)) {
            return;
        }

        AbstractServiceCache cache = null;
        try {
            cache = (AbstractServiceCache) Class.forName(classPath).getConstructor().newInstance();
        } catch (Throwable ignored) {
        }

        addService(cache);
    }

    private void addService(AbstractServiceCache cache) {
        if (cache == null) {
            return;
        }

        try {
            ServiceCache.addAllWithServiceNode(cache.getServices());
        } catch (Throwable ignored) {
        }
    }

}
