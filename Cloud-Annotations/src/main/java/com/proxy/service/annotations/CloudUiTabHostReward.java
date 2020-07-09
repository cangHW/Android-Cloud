package com.proxy.service.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标示自动装载
 * {@link com.proxy.service.api.services.CloudUiTabHostService}
 *
 * @author: cangHX
 * on 2020/06/29  14:32
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudUiTabHostReward {

    /**
     * 用于展示ui是进行过滤
     * {@link com.proxy.service.api.services.CloudUiTabHostService#showWithTag(String)}
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:36
     */
    String rewardTag() default "cloud_reward_normal";

}
