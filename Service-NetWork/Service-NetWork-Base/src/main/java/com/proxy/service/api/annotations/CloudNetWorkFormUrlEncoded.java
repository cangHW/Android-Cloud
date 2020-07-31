package com.proxy.service.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置本次请求参数需要进行 UrlEncoded
 *
 * @author : cangHX
 * on 2020/07/27  9:34 PM
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudNetWorkFormUrlEncoded {
}
