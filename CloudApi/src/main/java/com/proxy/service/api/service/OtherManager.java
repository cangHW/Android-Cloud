package com.proxy.service.api.service;

import com.proxy.service.api.service.cache.OtherCache;
import com.proxy.service.node.OtherNode;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/30  17:25
 */
public enum OtherManager {

    /**
     * 单例对象
     */
    INSTANCE;

    /**
     * 获取包含对应注解的类
     *
     * @param aClass : 注解class
     * @return 查询到的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-30 17:56
     */
    public List<Object> getOtherObjectByAnnotation(Class<? extends Annotation> aClass) {
        List<Object> list = new ArrayList<>();

        List<OtherNode> otherNodes = OtherCache.getAll();
        for (OtherNode node : otherNodes) {
            try {
                boolean isHas = node.object.getClass().isAnnotationPresent(aClass);
                if (isHas) {
                    list.add(checkIsNewInstance(node.object, node.isNewInstance));
                }
            } catch (Throwable ignored) {
            }
        }

        return list;
    }

    /**
     * 获取对应类以及其子类
     *
     * @param aClass : 对应类的class
     * @return 查询到的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-30 17:56
     */
    public List<Object> getOtherObject(Class<?> aClass) {
        List<Object> list = new ArrayList<>();

        List<OtherNode> otherNodes = OtherCache.getAll();
        for (OtherNode node : otherNodes) {
            if (!aClass.isAssignableFrom(node.object.getClass())) {
                //不存在继承关系，说明不是目标对象
                continue;
            }
            list.add(checkIsNewInstance(node.object, node.isNewInstance));
        }

        return list;
    }

    private Object checkIsNewInstance(Object o, boolean isNewInstance) {
        Object object = o;
        if (isNewInstance) {
            try {
                object = object.getClass().getConstructor().newInstance();
            } catch (Throwable ignored) {
            }
        }
        return object;
    }

}
