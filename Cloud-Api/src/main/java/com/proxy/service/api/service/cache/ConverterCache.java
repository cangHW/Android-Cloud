package com.proxy.service.api.service.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.api.service.listener.Converter;
import com.proxy.service.api.service.node.ListNode;
import com.proxy.service.base.BaseService;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 转换器仓库
 *
 * @author: cangHX
 * on 2020/06/08  17:44
 */
public class ConverterCache {

    /**
     * 转换器集合
     */
    private static final LinkedHashMap<Class<? extends BaseService>, ListNode> CONVERTERS_MAPPER = new LinkedHashMap<>();

    /**
     * 添加
     *
     * @param uuid      : 唯一ID，主要用于转换器
     * @param tClass    : 当前转换器对什么类型生效
     * @param converter : 转换器对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-08 17:56
     */
    public synchronized static <T extends BaseService> void add(@NonNull String uuid, @NonNull Class<T> tClass, @NonNull Converter<T> converter) {
        ListNode listNode = CONVERTERS_MAPPER.get(tClass);
        if (listNode == null) {
            listNode = new ListNode();
            CONVERTERS_MAPPER.put(tClass, listNode);
        }
        listNode.add(uuid, converter);
    }

    /**
     * 获取
     *
     * @param tClass : 转换器类型
     * @return 对应的转换器集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-08 17:56
     */
    @Nullable
    public synchronized static <T extends BaseService> ListNode get(@NonNull Class<T> tClass) {
        return CONVERTERS_MAPPER.get(tClass);
    }

    /**
     * 移除
     *
     * @param uuid : 唯一ID，主要用于转换器
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-08 17:57
     */
    public synchronized static void remove(@NonNull String uuid) {
        for (ListNode listNode : CONVERTERS_MAPPER.values()) {
            listNode.remove(uuid);
        }
    }

    /**
     * 移除
     *
     * @param tClass : 转换器类型
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-08 17:57
     */
    public synchronized static <T extends BaseService> void remove(@NonNull Class<T> tClass) {
        CONVERTERS_MAPPER.remove(tClass);
    }

    /**
     * key值集合
     *
     * @return key值集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-08 18:01
     */
    @NonNull
    public static HashSet<Class<? extends BaseService>> keySet() {
        Set<Class<? extends BaseService>> set = CONVERTERS_MAPPER.keySet();
        LinkedHashSet<Class<? extends BaseService>> hashSet = new LinkedHashSet<>(set.size(), 1);
        hashSet.addAll(set);
        return hashSet;
    }

}
