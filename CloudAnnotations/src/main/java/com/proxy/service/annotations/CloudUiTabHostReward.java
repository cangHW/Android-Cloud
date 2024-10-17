package com.proxy.service.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标示自动装载，ui 模块
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudUiTabHostReward {

    /**
     * 用于展示ui是进行过滤
     */
    String rewardTag() default "cloud_reward_normal";

}
