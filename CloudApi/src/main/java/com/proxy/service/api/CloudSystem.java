package com.proxy.service.api;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.service.DataManager;
import com.proxy.service.api.service.ServiceManager;
import com.proxy.service.api.service.cache.ConverterCache;
import com.proxy.service.api.service.cache.ServiceCache;
import com.proxy.service.api.service.listener.Converter;
import com.proxy.service.api.service.node.Node;
import com.proxy.service.api.log.Logger;
import com.proxy.service.api.log.LogInit;
import com.proxy.service.base.BaseService;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 微服务，对外管理类
 *
 * @author: cangHX
 * on 2020/06/04  16:10
 */
public final class CloudSystem {

    private static final AtomicBoolean IS_INIT = new AtomicBoolean(false);

    /**
     * 判断是否初始化
     *
     * @return true 已经初始化，false 未初始化
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 12:00
     */
    public synchronized static boolean isInit() {
        return IS_INIT.get();
    }

    /**
     * 初始化
     *
     * @param context : 上下文环境
     * @param isDebug : 是否是测试环境
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 15:53
     */
    public synchronized static void init(@NonNull Context context, boolean isDebug) {
        if (IS_INIT.compareAndSet(false, true)) {
            LogInit.INSTANCE.setIsDebug(isDebug);
            DataManager.INSTANCE.registerAllServices(context);
        } else {
            Logger.INSTANCE.d("It can only be init once");
        }
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
        ServiceCache.addAllWithBaseService(services);
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
        if (!list.isEmpty()) {
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
        if (!list.isEmpty()) {
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
     * 全局监控
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
     * 定向监控
     * 允许对CloudSystem的功能进行一定程度的扩展
     * 传入一个转换器对象，生效时机为数据返回之前
     *
     * @param uuid      : 唯一ID
     * @param tClass    : 当前转换器对什么类型生效
     * @param converter : 转换器对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/03/04 15:40
     */
    public static <T extends BaseService> void addConverter(@NonNull String uuid, @NonNull Class<T> tClass, @NonNull Converter<T> converter) {
        ConverterCache.add(uuid, tClass, converter);
    }

    /**
     * 允许对CloudSystem的功能进行一定程度的扩展
     * 根据服务类型移除对应的转换器对象
     *
     * @param tClass :  服务类型
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/03/04 15:40
     */
    public static <T extends BaseService> void removeConverterByClass(@NonNull Class<T> tClass) {
        ConverterCache.remove(tClass);
    }

    /**
     * 允许对CloudSystem的功能进行一定程度的扩展
     * 根据唯一 ID 移除对应的转换器对象
     *
     * @param uuid : 唯一ID
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/03/04 15:40
     */
    public static <T extends BaseService> void removeConverterByUuid(@NonNull String uuid) {
        ConverterCache.remove(uuid);
    }

}
