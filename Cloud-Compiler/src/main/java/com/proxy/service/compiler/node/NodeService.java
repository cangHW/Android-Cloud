package com.proxy.service.compiler.node;

import com.proxy.service.node.BaseNode;

/**
 * @author: cangHX
 * on 2020/06/30  10:06
 */
public class NodeService extends BaseNode {

    /**
     * 服务的tag
     */
    public String serviceTag;

    /**
     * 服务类地址
     */
    public String classPath;

    private NodeService(String serviceTag, boolean isNewInstance, String classPath) {
        this.serviceTag = serviceTag;
        this.isNewInstance = isNewInstance;
        this.classPath = classPath;
    }

    public static NodeService create(String serviceTag, boolean isNewInstance, String classPath) {
        return new NodeService(serviceTag, isNewInstance, classPath);
    }
}
