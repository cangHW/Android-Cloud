package com.cloud.api.service.cache;

import androidx.annotation.NonNull;

import com.cloud.annotations.CloudNewInstance;
import com.cloud.annotations.CloudService;
import com.cloud.api.utils.Logger;
import com.cloud.api.base.BaseService;
import com.cloud.api.node.ServiceNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/08  17:46
 * <p>
 * 服务类仓库
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
            CloudService cloudService = tClass.getAnnotation(CloudService.class);
            CloudNewInstance cloudNewInstance = tClass.getAnnotation(CloudNewInstance.class);
            if (cloudService == null) {
                Logger.Error(tClass.getSimpleName() + "缺少 CloudService 注解");
                continue;
            }
            list.add(new ServiceNode(cloudService.serviceTag(), cloudNewInstance != null, service));
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
