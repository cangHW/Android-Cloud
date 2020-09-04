package com.proxy.service.api.request.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置本次请求的 BaseUrl
 *
 * @author : cangHX
 * on 2020/07/19  5:39 PM
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudNetWorkBaseUrl {

    /**
     * BaseUrl
     */
    String value();

}
