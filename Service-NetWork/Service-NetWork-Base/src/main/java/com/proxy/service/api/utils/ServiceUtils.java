package com.proxy.service.api.utils;

import androidx.annotation.Nullable;

import com.proxy.service.api.error.CloudApiError;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/**
 * @author : cangHX
 * on 2020/7/23 9:09 PM
 */
public class ServiceUtils {

    /**
     * 检测类是否是合格的接口类，是否允许发起网络请求
     *
     * @param service : 即将被检测的类
     * @return 检测结果，true 符合条件，false 不符合
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/23 9:09 PM
     */
    public static boolean checkServiceInterface(@Nullable Class<?> service) {
        if (service == null) {
            Logger.Debug(CloudApiError.DATA_EMPTY.append("with : service").build());
            return false;
        }
        if (!service.isInterface()) {
            Logger.Debug(CloudApiError.DATA_TYPE_ERROR.setMsg("API declarations must be interfaces. with " + service.getCanonicalName()).build());
            return false;
        }
        if (service.getInterfaces().length > 0) {
            Logger.Debug(CloudApiError.DATA_TYPE_ERROR.setMsg("API interfaces must not extend other interfaces. with " + service.getCanonicalName()).build());
            return false;
        }
        return true;
    }

    /**
     * 检查返回值类型
     *
     * @param returnType : 返回值类型
     * @return 是否符合要求，true 符合，false 不符合
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/29 10:10 PM
     */
    public static boolean checkReturnType(@Nullable Type returnType) {
        if (returnType instanceof Class<?>) {
            return true;
        }
        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) returnType;
            Type[] types = parameterizedType.getActualTypeArguments();
            for (Type type : types) {
                if (!checkReturnType(type)) {
                    return false;
                }
            }
            return true;
        }
        if (returnType instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType) returnType;
            return checkReturnType(genericArrayType.getGenericComponentType());
        }
        if (returnType instanceof TypeVariable) {
            return false;
        }
        if (returnType instanceof WildcardType) {
            return false;
        }
        Logger.Warning("please check the return type. with : " + (returnType == null ? "null" : returnType.getClass().getName()));
        return false;
    }

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
    public static Type getParameterUpperBound(int index, ParameterizedType parameterizedType) {
        Type[] types = parameterizedType.getActualTypeArguments();
        if (index < 0 || index >= types.length) {
            Logger.Debug(CloudApiError.INDEX_OUT_OF_BOUNDS.setMsg("The total count : " + types.length + " and the index : " + index).build());
            return null;
        }
        Type type = types[index];
        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            return wildcardType.getUpperBounds()[0];
        }
        return type;
    }

    /**
     * 获取对象真实的 Class 类型
     *
     * @param type : 对象类型
     * @return 真实的 Class 类型
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:16 PM
     */
    @Nullable
    public static Class<?> getRawType(@Nullable Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class<?>) {
                return (Class<?>) rawType;
            }
            return null;
        }
        if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType) type;
            Type componentType = genericArrayType.getGenericComponentType();
            Class<?> tClass = getRawType(componentType);
            if (tClass == null) {
                return Array.class;
            }
            return Array.newInstance(tClass, 0).getClass();
        }
        if (type instanceof TypeVariable) {
            return Object.class;
        }
        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            return getRawType(wildcardType.getUpperBounds()[0]);
        }
        Logger.Warning(CloudApiError.DATA_EMPTY.append("with : type = null").build());
        return null;
    }
}
