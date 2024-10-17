package com.proxy.service.compiler.node;

import com.proxy.service.node.BaseNode;

/**
 * @author cangHX
 * on 2020/06/30  10:06
 */
public class NodeService extends BaseNode {

    /**
     * 服务的tag
     */
    public final String serviceTag;

    /**
     * 服务类地址
     */
    public final String classPath;

    private NodeService(String serviceTag, boolean isNewInstance, String classPath) {
        this.serviceTag = serviceTag;
        this.isNewInstance = isNewInstance;
        this.classPath = classPath;
    }

    /**
     * 创建节点服务
     * @param serviceTag 服务标识
     * @param isNewInstance 是否创建新对象
     * @param classPath class 地址
     * @return 节点服务
     * */
    public static NodeService create(String serviceTag, boolean isNewInstance, String classPath) {
        return new NodeService(serviceTag, isNewInstance, classPath);
    }
}
