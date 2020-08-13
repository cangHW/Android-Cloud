package com.proxy.service.utils.util;

import java.util.List;

/**
 * @author : cangHX
 * on 2020/08/10  10:37 PM
 */
public class Utils {

    /**
     * 填充数据
     *
     * @param list  : 待填充数组
     * @param t     : 默认数据
     * @param count : 填充数量
     * @return 填充后的数组
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/10 10:44 PM
     */
    @SuppressWarnings("UnusedReturnValue")
    public static synchronized <T> List<T> fill(List<T> list, T t, int count) {
        if (list == null) {
            return null;
        }
        for (int i = 0; i < count; i++) {
            if (list.size() <= i) {
                list.add(t);
            } else {
                list.set(i, t);
            }
        }
        return list;
    }

    /**
     * 填充数据
     *
     * @param list    : 待填充数组
     * @param ts      : 数据
     * @param start   : 开始位置
     * @param compare : 对比数据
     * @return 填充后的数组
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/10 10:44 PM
     */
    @SuppressWarnings("UnusedReturnValue")
    public static synchronized <T> List<T> fill(List<T> list, List<T> ts, int start, Object compare) {
        if (list == null || list.size() <= start) {
            return list;
        }
        if (ts == null || ts.size() == 0) {
            return list;
        }
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            T t1 = list.get(i);
            if (t1 == compare) {
                index++;
            }
            if (index == start) {
                list.remove(i);
                list.addAll(i, ts);
                break;
            }
        }
        return list;
    }

}
