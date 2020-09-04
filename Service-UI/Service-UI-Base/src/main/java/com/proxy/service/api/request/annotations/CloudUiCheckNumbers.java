package com.proxy.service.api.request.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置多个判断，吐司
 *
 * @author: cangHX
 * on 2020/07/08  13:38
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudUiCheckNumbers {

    /**
     * 多个判断
     */
    CloudUiCheckNumber[] value();

}
