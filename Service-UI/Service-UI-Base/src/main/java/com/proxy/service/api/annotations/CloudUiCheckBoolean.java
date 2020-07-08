package com.proxy.service.api.annotations;

import androidx.annotation.StringRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: cangHX
 * on 2020/07/07  16:33
 * <p>
 * 设置单个判断，吐司
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudUiCheckBoolean {

    /**
     * 标记id，标记当前变量
     */
    String markId();

    /**
     * 当前内容
     */
    boolean isValue() default false;

    /**
     * 校验失败后的吐司信息
     */
    String message() default "";

    /**
     * 校验失败后的吐司信息的资源 id，与 message 取其一，优先 message
     */
    @StringRes int stringId() default 0;
}
