package com.proxy.service.ui.util;

import java.util.List;

/**
 * @author: cangHX
 * on 2020/07/05  11:27
 */
public class CollectionUtils {

    /**
     * 填充数据
     *
     * @param list  : 准备填充数据的数组
     * @param count : 填充的数量
     * @param value : 填充的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-05 11:29
     */
    public static <T> void fullValue(List<T> list, int count, T value) {
        if (list == null) {
            return;
        }
        for (int i = 0; i < count; i++) {
            list.add(value);
        }
    }

}
