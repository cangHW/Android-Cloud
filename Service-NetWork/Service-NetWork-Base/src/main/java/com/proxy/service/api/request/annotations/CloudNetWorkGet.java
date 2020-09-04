package com.proxy.service.api.request.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置本次请求为 get 请求，并传入 url path
 *
 * @author : cangHX
 * on 2020/07/19  5:41 PM
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudNetWorkGet {

    /**
     * url path
     */
    String value();

}
