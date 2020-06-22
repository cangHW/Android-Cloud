package com.proxy.service.api.service;

import android.content.Context;

import androidx.annotation.NonNull;

import com.proxy.service.api.CloudSystem;
import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.service.cache.ConverterCache;
import com.proxy.service.api.service.cache.ServiceCache;
import com.proxy.service.api.utils.Logger;
import com.proxy.service.api.service.listener.Converter;
import com.proxy.service.api.service.node.ListNode;
import com.proxy.service.api.service.node.Node;
import com.proxy.service.api.multidex.ClassUtils;
import com.proxy.service.base.AbstractServiceCache;
import com.proxy.service.base.BaseService;
import com.proxy.service.consts.ClassConstants;
import com.proxy.service.node.ServiceNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: cangHX
 * on 2020/06/04  16:14
 * <p>
 * service管理类
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
            Logger.Error(CloudApiError.NO_INIT.build());
            return Collections.emptyList();
        }
        //安全性处理，防止在使用的过程中，mServices 的长度被恶意修改
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
            Logger.Error(CloudApiError.NO_INIT.build());
            return Collections.emptyList();
        }
        //安全性处理，防止在使用的过程中，mServices 的长度被恶意修改
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
     * 通过辅助文件，加载所有可以加载的服务
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 18:01
     */
    private void findAllServices(Context context) {
        try {
            Set<String> stringSet = ClassUtils.getFileNameByPackageName(context, ClassConstants.PACKAGE_SERVICES_CACHE);
            if (stringSet == null) {
                return;
            }
            for (String classPath : stringSet) {
                if (!classPath.startsWith(ClassConstants.PACKAGE_SERVICES_CACHE + "." + ClassConstants.CLASS_PREFIX)) {
                    continue;
                }
                try {
                    AbstractServiceCache cache = (AbstractServiceCache) Class.forName(classPath).getConstructor().newInstance();
                    ServiceCache.addAll(cache.getServices());
                } catch (Throwable ignored) {
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
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
                service.getClass().getConstructor().newInstance();
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
        HashSet<Class> set = ConverterCache.keySet();
        for (Class tClass : set) {
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

                Converter converter = node.getConverter();
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
