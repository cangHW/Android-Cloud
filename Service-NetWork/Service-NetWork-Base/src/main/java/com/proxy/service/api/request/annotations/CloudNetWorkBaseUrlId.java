package com.proxy.service.api.request.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置本次请求的 BaseUrl 的 id，方便对 BaseUrl 进行动态替换
 *
 * @author : cangHX
 * on 2020/07/19  5:39 PM
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudNetWorkBaseUrlId {

    /**
     * BaseUrl Id
     */
    String value();

}
