package com.cloud.api.node;

import com.cloud.api.base.BaseService;

/**
 * @author: cangHX
 * on 2020/06/05  18:12
 */
public class ServiceNode {

    /**
     * 服务的tag
     */
    public String serviceTag;
    /**
     * 是否每次使用时都新创建一个对象
     */
    public boolean isNewInstance;

    /**
     * 当前服务类实例
     */
    public BaseService service;

    public ServiceNode(String serviceTag, boolean isNewInstance, BaseService service) {
        this.serviceTag = serviceTag;
        this.isNewInstance = isNewInstance;
        this.service = service;
    }
}
