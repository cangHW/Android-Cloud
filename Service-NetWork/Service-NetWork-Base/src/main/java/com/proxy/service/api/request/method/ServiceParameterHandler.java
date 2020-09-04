package com.proxy.service.api.request.method;

import com.proxy.service.api.request.annotations.CloudNetWorkField;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author : cangHX
 * on 2020/07/28  10:44 PM
 */
public abstract class ServiceParameterHandler<T> {

    /**
     * 数据处理
     *
     * @param map : 处理好的数据集合
     * @param t   : 待处理数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 10:15 AM
     */
    public abstract void apply(Map<String, String> map, T t);


    public static class CloudNetWorkFieldHandler extends ServiceParameterHandler<String> {

        private Type mParameterType;
        private CloudNetWorkField mAnnotation;

        public CloudNetWorkFieldHandler(Type parameterType, CloudNetWorkField annotation) {
            this.mParameterType = parameterType;
            this.mAnnotation = annotation;
        }

        /**
         * 数据处理
         *
         * @param map : 处理好的数据集合
         * @param s   : 待处理数据
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/8/3 10:15 AM
         */
        @Override
        public void apply(Map<String, String> map, String s) {
            map.put(mAnnotation.value(), s);
        }
    }

}
