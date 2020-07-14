package com.proxy.service.compiler.node;

import com.proxy.service.node.BaseNode;

/**
 * @author: cangHX
 * on 2020/06/30  10:44
 */
public class NodeOther extends BaseNode {

    /**
     * 服务类地址
     */
    public final String classPath;

    private NodeOther(boolean isNewInstance, String classPath) {
        this.isNewInstance = isNewInstance;
        this.classPath = classPath;
    }

    public static NodeOther create(boolean isNewInstance, String classPath) {
        return new NodeOther(isNewInstance, classPath);
    }
}
