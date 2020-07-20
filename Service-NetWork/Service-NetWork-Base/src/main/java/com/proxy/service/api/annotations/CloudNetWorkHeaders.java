package com.proxy.service.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 为本次请求添加多个 header
 *
 * @author : cangHX
 * on 2020/07/19  5:41 PM
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudNetWorkHeaders {

    /**
     * 多个 header
     */
    CloudNetWorkHeader[] value();

}
