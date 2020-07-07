package com.proxy.service.api.annotations;

import androidx.annotation.StringRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: cangHX
 * on 2020/07/07  16:33
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudUiCheckNumber {

    /**
     * 标记id，标记当前变量
     */
    String markId();

    /**
     * 最大值
     */
    double max() default Double.MAX_VALUE;

    /**
     * 最小值
     */
    double min() default Double.MIN_VALUE;

    /**
     * 小数长度
     */
    int scale() default 0;

    /**
     * 校验失败后的吐司信息
     */
    String message() default "";

    /**
     * 校验失败后的吐司信息的资源 id，与 message 取其一，优先 message
     */
    @StringRes int StringId() default 0;
}
