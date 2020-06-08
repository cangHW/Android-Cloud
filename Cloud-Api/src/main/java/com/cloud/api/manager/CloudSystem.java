package com.cloud.api.manager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cloud.api.manager.cache.ConverterCache;
import com.cloud.api.manager.cache.ServiceCache;
import com.cloud.base.base.BaseService;
import com.cloud.api.manager.listener.Converter;
import com.cloud.api.manager.node.Node;

import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/04  16:10
 * <p>
 * 微服务，对外管理类
 */
public class CloudSystem {

    /**
     * 初始化
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 15:53
     */
    public static void init(Context context) {
        ServiceManager.INSTANCE.registerAllServices(context);
    }

    /**
     * 根据传入 tag 值，返回符合条件的 service 实例
     * service 实例在当前条件下可能存在多个，默认返回查找到的第一个
     *
     * @param tag : 准备查找的 service 实例的 tag
     * @return T : 查找到的 service 实例
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 15:59
     */
    @Nullable
    public static <T extends BaseService> T getService(@NonNull String tag) {
        return getServiceWithUuid(Node.UUID_DEFAULT, tag);
    }

    /**
     * 根据传入 tag 值，返回符合条件的 service 实例
     * service 实例在当前条件下可能存在多个，默认返回查找到的第一个
     *
     * @param uuid : 唯一ID，主要用于转换器
     * @param tag  : 准备查找的 service 实例的 tag
     * @return T : 查找到的 service 实例
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 15:59
     */
    @Nullable
    public static <T extends BaseService> T getServiceWithUuid(@NonNull String uuid, @NonNull String tag) {
        List<T> list = ServiceManager.INSTANCE.getService(uuid, tag, ServiceManager.TYPE_ONLY);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据传入 tag 值，返回所有符合条件的 service 实例
     *
     * @param tag : 准备查找的 service 实例的 tag
     * @return T : 查找到的所有 service 实例
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 15:59
     */
    @NonNull
    public static <T extends BaseService> List<T> getServices(@NonNull String tag) {
        return getServicesWithUuid(Node.UUID_DEFAULT, tag);
    }

    /**
     * 根据传入 tag 值，返回所有符合条件的 service 实例
     *
     * @param uuid : 唯一ID，主要用于转换器
     * @param tag  : 准备查找的 service 实例的 tag
     * @return T : 查找到的所有 service 实例
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 15:59
     */
    @NonNull
    public static <T extends BaseService> List<T> getServicesWithUuid(@NonNull String uuid, @NonNull String tag) {
        return ServiceManager.INSTANCE.getService(uuid, tag, ServiceManager.TYPE_ALL);
    }


    /**
     * 根据传入 Class 类型，返回符合条件的 service 实例
     * service 实例在当前条件下可能存在多个，默认返回查找到的第一个
     *
     * @param tClass : 准备查找的 service 实例的 class 类型
     * @return T : 查找到的 service 实例
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 15:59
     */
    @Nullable
    public static <T extends BaseService> T getService(@NonNull Class<T> tClass) {
        return getServiceWithUuid(Node.UUID_DEFAULT, tClass);
    }

    /**
     * 根据传入 Class 类型，返回符合条件的 service 实例
     * service 实例在当前条件下可能存在多个，默认返回查找到的第一个
     *
     * @param uuid   : 唯一ID，主要用于转换器
     * @param tClass : 准备查找的 service 实例的 class 类型
     * @return T : 查找到的 service 实例
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 15:59
     */
    @Nullable
    public static <T extends BaseService> T getServiceWithUuid(@NonNull String uuid, @NonNull Class<T> tClass) {
        List<T> list = ServiceManager.INSTANCE.getService(uuid, tClass, ServiceManager.TYPE_ONLY);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据传入 Class 类型，返回所有符合条件的 service 实例
     *
     * @param tClass : 准备查找的 service 实例的 class 类型
     * @return List<T> : 查找到的所有 service 实例
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 15:59
     */
    @NonNull
    public static <T extends BaseService> List<T> getServices(@NonNull Class<T> tClass) {
        return getServicesWithUuid(Node.UUID_DEFAULT, tClass);
    }

    /**
     * 根据传入 Class 类型，返回所有符合条件的 service 实例
     *
     * @param uuid   : 唯一ID，主要用于转换器
     * @param tClass : 准备查找的 service 实例的 class 类型
     * @return List<T> : 查找到的所有 service 实例
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 15:59
     */
    @NonNull
    public static <T extends BaseService> List<T> getServicesWithUuid(@NonNull String uuid, @NonNull Class<T> tClass) {
        return ServiceManager.INSTANCE.getService(uuid, tClass, ServiceManager.TYPE_ALL);
    }

    /**
     * 供开发者使用，提供外部注册服务功能
     *
     * @param services : 外部准备注册的服务集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 18:32
     */
    public static void registerServices(@NonNull List<BaseService> services) {
        ServiceCache.addAllAtFirst(services);
    }

    /**
     * 允许对CloudSystem的功能进行一定程度的扩展
     * 传入一个转换器对象，生效时机为数据返回之前
     *
     * @param tClass    : 当前转换器对什么类型生效
     * @param converter : 转换器对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/03/04 15:40
     */
    public static <T extends BaseService> void addConverter(@NonNull Class<T> tClass, @NonNull Converter<T> converter) {
        addConverter(Node.UUID_DEFAULT, tClass, converter);
    }

    /**
     * 允许对CloudSystem的功能进行一定程度的扩展
     * 传入一个转换器对象，生效时机为数据返回之前
     *
     * @param uuid      : 唯一ID，主要用于转换器
     * @param tClass    : 当前转换器对什么类型生效
     * @param converter : 转换器对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/03/04 15:40
     */
    public static <T extends BaseService> void addConverter(@NonNull String uuid, @NonNull Class<T> tClass, @NonNull Converter<T> converter) {
        ConverterCache.add(uuid,tClass,converter);
    }

    /**
     * 允许对CloudSystem的功能进行一定程度的扩展
     * 根据类型移除对应的转换器对象
     *
     * @param tClass :  转换器类型
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/03/04 15:40
     */
    public static <T extends BaseService> void removeConverterByClass(@NonNull Class<T> tClass) {
        ConverterCache.remove(tClass);
    }

    /**
     * 允许对CloudSystem的功能进行一定程度的扩展
     * 根据类型移除对应的转换器对象
     *
     * @param uuid : 唯一ID，主要用于转换器
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/03/04 15:40
     */
    public static <T extends BaseService> void removeConverterByUuid(@NonNull String uuid) {
        ConverterCache.remove(uuid);
    }

}
