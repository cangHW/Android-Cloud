package com.proxy.service.api.service;

import androidx.annotation.NonNull;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.service.cache.ConverterCache;
import com.proxy.service.api.service.cache.ServiceCache;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.api.service.listener.Converter;
import com.proxy.service.api.service.node.ListNode;
import com.proxy.service.api.service.node.Node;
import com.proxy.service.base.BaseService;
import com.proxy.service.node.ServiceNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * service管理类
 *
 * @author: cangHX
 * on 2020/06/04  16:14
 */
@SuppressWarnings("unchecked")
public enum ServiceManager {
    /**
     * 单例对象
     */
    INSTANCE;

    /**
     * 查询全部
     */
    public static final String TYPE_ALL = "all";
    /**
     * 查询单个
     */
    public static final String TYPE_ONLY = "only";

    /**
     * 通过 tag 值查找对应的 Service 集合
     *
     * @param uuid : 唯一ID，主要用于转换器
     * @param tag  : 准备查找的 service 实例的 tag
     * @param type : 查找的范围类型
     * @return List<T> : 返回的数据集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 18:15
     */
    @NonNull
    public synchronized <T extends BaseService> List<T> getService(@NonNull String uuid, @NonNull String tag, @NonNull String type) {
        if (!CloudSystem.isInit()) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return Collections.emptyList();
        }
        final List<ServiceNode> services = ServiceCache.getAll();
        List<T> list = new ArrayList<>();
        for (ServiceNode node : services) {
            if (!tag.equals(node.serviceTag)) {
                //tag 不符合，说明不是目标对象
                continue;
            }
            if (doCheck(uuid, node, list, type)) {
                break;
            }
        }
        return list;
    }

    /**
     * 通过 Service 类型查找对应的 Service 集合
     *
     * @param uuid   : 唯一ID，主要用于转换器
     * @param tClass : 准备查找的 service 实例的 class 类型
     * @param type   : 查找的范围类型
     * @return List<T> : 返回的数据集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 18:17
     */
    @NonNull
    public synchronized <T extends BaseService> List<T> getService(@NonNull String uuid, @NonNull Class<T> tClass, @NonNull String type) {
        if (!CloudSystem.isInit()) {
            Logger.Error(CloudApiError.INIT_EMPTY.build());
            return Collections.emptyList();
        }
        final List<ServiceNode> services = ServiceCache.getAll();
        List<T> list = new ArrayList<>();
        for (ServiceNode node : services) {
            if (!tClass.isAssignableFrom(node.service.getClass())) {
                //不存在继承关系，说明不是目标对象
                continue;
            }
            if (doCheck(uuid, node, list, type)) {
                break;
            }
        }
        return list;
    }

    /**
     * 检查当前服务是否符合要求，以及是否要提前结束查询
     *
     * @param uuid : 唯一ID，主要用于转换器
     * @param node : 服务对象信息
     * @param type : 查找的范围类型
     * @param list : 获取到的服务集合
     * @return 是否提前结束查询，true 结束，false 继续
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-08 18:51
     */
    private <T extends BaseService> boolean doCheck(String uuid, ServiceNode node, List<T> list, String type) {
        BaseService service = node.service;
        if (node.isNewInstance) {
            try {
                service = service.getClass().getConstructor().newInstance();
            } catch (Throwable ignored) {
            }
        }
        try {
            //执行转换器逻辑
            service = doConverter(uuid, service);
            if (service != null) {
                list.add((T) service);
            }
        } catch (Throwable ignored) {
        }
        return list.size() > 0 && TYPE_ONLY.equals(type);
    }

    /**
     * 检查当前service对象是否有符合的转换器
     *
     * @param uuid    : 唯一ID，主要用于转换器
     * @param service : 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/03/04 18:01
     */
    private BaseService doConverter(String uuid, BaseService service) {
        HashSet<Class<? extends BaseService>> set = ConverterCache.keySet();
        for (Class<? extends BaseService> tClass : set) {
            if (!tClass.isAssignableFrom(service.getClass())) {
                continue;
            }

            ListNode listNode = ConverterCache.get(tClass);
            if (listNode == null || listNode.size() == 0) {
                ConverterCache.remove(tClass);
                continue;
            }

            for (Node node : listNode.list()) {
                String string = node.getUuid();

                if (!string.equals(Node.UUID_DEFAULT) && !string.equals(uuid)) {
                    continue;
                }

                Converter<BaseService> converter = (Converter<BaseService>) node.getConverter();
                if (converter == null) {
                    listNode.remove(node);
                    continue;
                }
                try {
                    service = converter.converter(service);
                } catch (Throwable ignored) {
                }
            }
        }
        return service;
    }
}
