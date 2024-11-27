package com.proxy.service.api.callback.request;

import com.proxy.service.api.log.ServiceUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author : cangHX
 * on 2020/07/27  7:26 PM
 */
public interface CloudNetWorkCallAdapter<A, T> {

    /**
     * 获取真实返回值类型
     *
     * @return 真实返回值类型
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/31 10:24 PM
     */
    Type responseType();

    /**
     * 获取返回类型
     *
     * @param call : 请求接口
     * @return 返回类型
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/31 10:24 PM
     */
    A adapt(CloudNetWorkCall<T> call);

    abstract class Factory {

        /**
         * 获取支持当前返回类型的 adapter
         *
         * @param returnType : 返回值类型
         * @return 支持的 adapter 对象
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/7/30 9:59 PM
         */
        public abstract CloudNetWorkCallAdapter<?, ?> get(Type returnType);

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
