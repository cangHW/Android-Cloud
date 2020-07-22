package com.proxy.service.api.service.cache;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiNewInstance;
import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.base.BaseService;
import com.proxy.service.node.ServiceNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务类仓库
 *
 * @author: cangHX
 * on 2020/06/08  17:46
 */
public class ServiceCache {

    /**
     * 默认放置位置为队尾
     */
    private static final int INDEX_DEFAULT = -1;
    /**
     * 服务集合类
     */
    private static final List<ServiceNode> SERVICES = new ArrayList<>();

    /**
     * 获取数据量
     *
     * @return 返回数量
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-08 18:42
     */
    public static int size() {
        return SERVICES.size();
    }

    /**
     * 注册
     *
     * @param services : 注册的服务集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-08 18:17
     */
    public synchronized static void addAllAtFirst(@NonNull List<BaseService> services) {
        //剔除用户传入的为null的数据
        List<ServiceNode> list = new ArrayList<>();
        for (BaseService service : services) {
            if (service == null) {
                continue;
            }
            Class<?> tClass = service.getClass();
            CloudApiService cloudApiService = tClass.getAnnotation(CloudApiService.class);
            CloudApiNewInstance cloudApiNewInstance = tClass.getAnnotation(CloudApiNewInstance.class);
            if (cloudApiService == null) {
                Logger.Error(tClass.getSimpleName() + "缺少 CloudService 注解");
                continue;
            }
            list.add(new ServiceNode(cloudApiService.serviceTag(), cloudApiNewInstance != null, service));
        }
        add(0, list);
    }

    /**
     * 注册
     *
     * @param services : 注册的服务集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-08 18:17
     */
    public synchronized static void addAll(@NonNull List<ServiceNode> services) {
        add(INDEX_DEFAULT, services);
    }

    /**
     * 注册（放置在数组前面）
     *
     * @param index    : 放置位置，{@link #INDEX_DEFAULT} 为末尾
     * @param services : 注册的服务集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-08 18:17
     */
    private synchronized static void add(int index, @NonNull List<ServiceNode> services) {
        if (index == INDEX_DEFAULT) {
            SERVICES.addAll(services);
            return;
        }
        if (size() >= index) {
            SERVICES.addAll(index, services);
        }
    }

    /**
     * 获取
     *
     * @return 获取全部数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-08 18:21
     */
    public synchronized static List<ServiceNode> getAll() {
        return new ArrayList<>(SERVICES);
    }

}
