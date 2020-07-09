package com.proxy.service.api.annotations;

import androidx.annotation.StringRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置单个判断，吐司
 *
 * @author: cangHX
 * on 2020/07/07  16:33
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudUiCheckString {

    /**
     * 标记id，标记当前变量
     */
    String markId();

    /**
     * 最大长度，<=
     */
    int maxLength() default -1;

    /**
     * 最大长度，<
     */
    int maxLengthNotSame() default -1;

    /**
     * 最小值，>=
     */
    int minLength() default -1;

    /**
     * 最小值，>
     */
    int minLengthNotSame() default -1;

    /**
     * 不能为 null 或 空
     */
    boolean notEmpty() default false;

    /**
     * 不能为空格
     */
    boolean notBlank() default false;

    /**
     * 校验失败后的吐司信息
     */
    String message() default "";

    /**
     * 校验失败后的吐司信息的资源 id，与 message 取其一，优先 message
     */
    @StringRes int stringId() default 0;
}
