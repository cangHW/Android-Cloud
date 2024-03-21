package com.proxy.service.node;

/**
 * @author cangHX
 * on 2020/06/30  09:59
 */
public class OtherNode extends BaseNode {

    /**
     * 未知数据
     */
    public final Object object;

    /**
     * 构造
     * @param object 参数
     * @param isNewInstance 是否创建新对象
     * */
    public OtherNode(boolean isNewInstance, Object object) {
        this.isNewInstance = isNewInstance;
        this.object = object;
    }

}
