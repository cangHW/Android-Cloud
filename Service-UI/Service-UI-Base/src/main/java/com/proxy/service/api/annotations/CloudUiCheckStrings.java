package com.proxy.service.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: cangHX
 * on 2020/07/08  13:38
 * <p>
 * 设置多个判断，吐司
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudUiCheckStrings {

    /**
     * 多个判断
     */
    CloudUiCheckString[] value();

}
