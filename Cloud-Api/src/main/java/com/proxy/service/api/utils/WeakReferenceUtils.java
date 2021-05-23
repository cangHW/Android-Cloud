package com.proxy.service.api.utils;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/08/19  9:22 PM
 */
public class WeakReferenceUtils {

    public interface Callback<T> {
        /**
         * 当数据不为空时回调
         *
         * @param t : 不为空的数据
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/8/26 9:56 PM
         */
        void onCallback(WeakReference<T> weakReference, T t);
    }

    /**
     * 检测弱引用对象是否为空
     *
     * @param weakReference : 弱引用
     * @param callback      : 回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/19 10:45 PM
     */
    public static <T> void checkValueIsEmpty(WeakReference<T> weakReference, @NonNull Callback<T> callback) {
        if (weakReference == null) {
            return;
        }
        T t = weakReference.get();
        if (t == null) {
            return;
        }
        callback.onCallback(weakReference, t);
    }

    /**
     * 检测弱引用集合内数据是否为空
     *
     * @param list     : 弱引用集合
     * @param callback : 回调接口
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/19 10:45 PM
     */
    public static <T> void checkValueIsEmpty(List<WeakReference<T>> list, @NonNull Callback<T> callback) {
        if (list == null) {
            return;
        }
        for (WeakReference<T> weakReference : new ArrayList<>(list)) {
            if (weakReference == null) {
                list.remove(null);
                return;
            }
            T t = weakReference.get();
            if (t == null) {
                list.remove(weakReference);
                return;
            }
            callback.onCallback(weakReference, t);
        }
    }

    /**
     * 检测是否存在相同的 value
     *
     * @param list     : 弱引用集合
     * @param nowValue : 用于对比的 value
     * @return true 存在，false 不存在
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/31 3:42 PM
     */
    public static <T> boolean checkValueIsSame(List<WeakReference<T>> list, T nowValue) {
        if (list == null) {
            return false;
        }
        for (WeakReference<T> weakReference : new ArrayList<>(list)) {
            if (weakReference == null) {
                list.remove(null);
                continue;
            }
            T t = weakReference.get();
            if (t == null) {
                list.remove(weakReference);
                continue;
            }
            if (t == nowValue) {
                return true;
            }
        }
        return false;
    }
}
