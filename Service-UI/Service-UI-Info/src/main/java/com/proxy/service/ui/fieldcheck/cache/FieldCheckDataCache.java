package com.proxy.service.ui.fieldcheck.cache;

import android.util.LruCache;

import com.proxy.service.ui.fieldcheck.node.BaseFieldCheckNode;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @author: cangHX
 * on 2020/07/09  14:20
 */
public enum FieldCheckDataCache {

    /**
     * 单例
     */
    INSTANCE;

    private final WeakHashMap<Class<?>, List<BaseFieldCheckNode>> mHashMap = new WeakHashMap<>();

    private final LruCache<Class<?>, List<BaseFieldCheckNode>> mLruCache = new LruCache<Class<?>, List<BaseFieldCheckNode>>(5) {
        @Override
        protected void entryRemoved(boolean evicted, Class<?> key, List<BaseFieldCheckNode> oldValue, List<BaseFieldCheckNode> newValue) {
            if (oldValue != null) {
                mHashMap.put(key, oldValue);
            }
        }
    };

    public void put(Class<?> key, List<BaseFieldCheckNode> value) {
        mLruCache.put(key, value);
    }

    public List<BaseFieldCheckNode> get(Class<?> key) {
        List<BaseFieldCheckNode> list = null;

        List<BaseFieldCheckNode> nodes = mLruCache.get(key);
        if (nodes == null) {
            if (mHashMap.containsKey(key)) {
                nodes = mHashMap.get(key);
                mHashMap.remove(key);
            }
            if (nodes != null) {
                mLruCache.put(key, nodes);
            }
        }
        if (nodes != null) {
            list = new ArrayList<>(nodes);
        }

        return list;
    }
}
