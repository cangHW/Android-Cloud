package com.proxy.service.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 为本次请求添加 header
 *
 * @author : cangHX
 * on 2020/07/19  5:40 PM
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudNetWorkHeader {

    /**
     * key 值
     */
    String key();

    /**
     * value 值
     */
    String value();

}
