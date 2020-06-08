package com.cloud.base.node;

import com.cloud.base.base.BaseService;

/**
 * @author: cangHX
 * on 2020/06/05  18:12
 */
public class ServiceNode extends BaseNode {

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
