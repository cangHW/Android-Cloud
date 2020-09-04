package com.proxy.service.api.request.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 为本次请求添加自定义 tag
 *
 * @author : cangHX
 * on 2020/07/19  5:49 PM
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudNetWorkTag {

    /**
     * 自定义 tag
     */
    String value();

}
