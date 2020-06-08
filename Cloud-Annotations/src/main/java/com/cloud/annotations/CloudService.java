package com.cloud.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: cangHX
 * on 2020/06/05  11:11
 * <p>
 * 标示一个类为对外提供的服务实现类
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloudService {

    /**
     * 服务的 tag
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 17:38
     */
    String serviceTag();
}
