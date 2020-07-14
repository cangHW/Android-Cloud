package com.proxy.service.node;


import com.proxy.service.base.BaseService;

/**
 * @author: cangHX
 * on 2020/06/05  18:12
 */
public class ServiceNode extends BaseNode {

    /**
     * 服务的tag
     */
    public final String serviceTag;

    /**
     * 当前服务类实例
     */
    public final BaseService service;

    public ServiceNode(String serviceTag, boolean isNewInstance, BaseService service) {
        this.serviceTag = serviceTag;
        this.isNewInstance = isNewInstance;
        this.service = service;
    }
}
