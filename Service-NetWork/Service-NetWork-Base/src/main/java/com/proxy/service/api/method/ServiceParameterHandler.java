package com.proxy.service.api.method;

import com.proxy.service.api.annotations.CloudNetWorkField;

import java.lang.reflect.Type;

/**
 * @author : cangHX
 * on 2020/07/28  10:44 PM
 */
public abstract class ServiceParameterHandler {





    public static class CloudNetWorkFieldHandler extends ServiceParameterHandler {

        public CloudNetWorkFieldHandler(Type parameterType, CloudNetWorkField annotation){

        }

    }

}
