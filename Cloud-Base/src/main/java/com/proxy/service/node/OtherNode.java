package com.proxy.service.node;

/**
 * @author: cangHX
 * on 2020/06/30  09:59
 */
public class OtherNode extends BaseNode {

    /**
     * 未知数据
     */
    public final Object object;

    public OtherNode(boolean isNewInstance, Object object) {
        this.isNewInstance = isNewInstance;
        this.object = object;
    }

}
