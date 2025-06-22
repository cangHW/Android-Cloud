package com.proxy.service.api.service.cache;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiNewInstance;
import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.log.Logger;
import com.proxy.service.base.BaseService;
import com.proxy.service.node.ServiceNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 服务类仓库
 *
 * @author: cangHX
 * on 2020/06/08  17:46
 */
public class ServiceCache {

    private static final Object obj = new Object();

    /**
     * 服务集合类
     */
    private static final LinkedHashMap<ServiceNode, Object> SERVICES = new LinkedHashMap<>(0, 0.75f, true);

    /**
     * 注册
     *
     * @param services : 注册的服务集合
     */
    public synchronized static void addAllWithBaseService(@NonNull List<BaseService> services) {
        //剔除用户传入的为 null 的数据
        List<BaseService> list = new ArrayList<>(services);
        Collections.reverse(list);
        for (BaseService service : list) {
            if (service == null) {
                continue;
            }
            Class<?> tClass = service.getClass();
            CloudApiService cloudApiService = tClass.getAnnotation(CloudApiService.class);
            CloudApiNewInstance cloudApiNewInstance = tClass.getAnnotation(CloudApiNewInstance.class);
            if (cloudApiService == null) {
                Logger.e(tClass.getSimpleName() + "缺少 CloudService 注解");
                continue;
            }
            ServiceNode node = new ServiceNode(cloudApiService.serviceTag(), cloudApiNewInstance != null, service);
            SERVICES.put(node, obj);
        }
    }

    /**
     * 注册
     *
     * @param services : 注册的服务集合
     */
    public synchronized static void addAllWithServiceNode(@NonNull List<ServiceNode> services) {
        for (ServiceNode node : new ArrayList<>(services)) {
            SERVICES.put(node, obj);
        }
    }

    /**
     * 获取
     *
     * @return 获取全部数据
     */
    public synchronized static List<ServiceNode> getAll() {
        ArrayList<ServiceNode> list = new ArrayList<>(SERVICES.keySet());
        Collections.reverse(list);
        return list;
    }

    /**
     * 更新顺序，基于 lru 策略
     *
     * @param node : 要更新顺序的对象
     */
    public synchronized static void update(ServiceNode node) {
        SERVICES.get(node);
    }
}
