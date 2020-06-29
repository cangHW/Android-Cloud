package com.proxy.service.api.annotations;

/**
 * @author: cangHX
 * on 2020/06/29  14:32
 * <p>
 * 用于标示自动装载
 * @see com.proxy.service.api.services.CloudUiTabHostService
 */
public @interface CloudUiTabHostReward {

    /**
     * 用于展示ui是进行过滤
     * {@link com.proxy.service.api.services.CloudUiTabHostService#showWithTag(String)}
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-29 14:36
     */
    String rewardTag() default "cloud_normal";

}
