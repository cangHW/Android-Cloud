package com.proxy.service.api.log;

import java.util.List;

/**
 * @author : cangHX
 * on 2021/06/02  8:29 PM
 */
public class ListUtils {

    public interface ListComparator<K, T> {
        boolean comparator(K k, T t);
    }

    /**
     * 根据条件，从list集合中获取数据
     *
     * @param list      : list集合
     * @param t         : 条件
     * @param converter : 检查器
     * @return 符合条件的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2021/6/2 10:36 AM
     */
    public static <K, T> K getValueWhen(List<K> list, T t, ListComparator<K, T> converter) {
        if (converter == null || list == null || list.size() == 0 || t == null) {
            return null;
        }
        for (K k : list) {
            if (k == null) {
                continue;
            }
            if (converter.comparator(k, t)) {
                return k;
            }
        }
        return null;
    }


}
