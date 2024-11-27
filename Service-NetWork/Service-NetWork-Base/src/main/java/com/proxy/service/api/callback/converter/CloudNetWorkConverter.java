package com.proxy.service.api.callback.converter;

import androidx.annotation.Nullable;

import com.proxy.service.api.log.ServiceUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author : cangHX
 * on 2020/08/03  7:33 PM
 */
public interface CloudNetWorkConverter<T> {

    /**
     * 数据转换
     *
     * @param value : 待转换数据
     * @return 转换后的数据
     * @throws IOException 转换过程中的异常
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/3 7:36 PM
     */
    @Nullable
    T convert(@Nullable String value) throws IOException;

    abstract class Factory {
        /**
         * response 转换器
         *
         * @param type : 数据类型
         * @return 支持的转换器
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/8/3 7:54 PM
         */
        public abstract CloudNetWorkConverter<?> responseBodyConverter(Type type);

        /**
         * 根据返回数据格式，获取参数格式
         *
         * @param index             : 坐标
         * @param parameterizedType : 返回数据格式
         * @return 参数格式
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/7/30 10:02 PM
         */
        protected static Type getParameterUpperBound(int index, ParameterizedType parameterizedType) {
            return ServiceUtils.getParameterUpperBound(index, parameterizedType);
        }

        /**
         * 获取对象真实的 Class 类型
         *
         * @param type : 对象类型
         * @return 真实的 Class 类型
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/7/30 10:07 PM
         */
        protected static Class<?> getRawType(Type type) {
            return ServiceUtils.getRawType(type);
        }
    }

}
