package com.proxy.service.api.method;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : cangHX
 * on 2020/7/24 8:57 PM
 */
public class ServiceMethodCache {

    private static final Map<Method, ServiceMethod> SERVICE_METHOD_CACHE = new ConcurrentHashMap<>();

    public static ServiceMethod loadServiceMethod(Method method) {
        ServiceMethod serviceMethod = SERVICE_METHOD_CACHE.get(method);
        if (serviceMethod != null) {
            return serviceMethod;
        }
        synchronized (SERVICE_METHOD_CACHE) {
            serviceMethod = SERVICE_METHOD_CACHE.get(method);
            if (serviceMethod == null) {
                serviceMethod = ServiceMethod.parseAnnotations(method);
                SERVICE_METHOD_CACHE.put(method, serviceMethod);
            }
        }
        return serviceMethod;
    }
}
