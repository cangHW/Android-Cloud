package com.cloud.base.base;

import com.cloud.base.node.ServiceNode;

import java.util.List;

/**
 * @author: cangHX
 * on 2019/10/31  17:40
 * <p>
 * 供编译器使用，生成辅助文件
 * 通过辅助文件，可以方便的获取对应的 Service 实例
 */
public abstract class AbstractServiceCache {

    /**
     * 获取当前辅助文件所包含的 Service 实例
     *
     * @return List<ServiceNode> : 当前辅助文件所包含的 Service 实例信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2019/10/31 17:42
     */
    public abstract List<ServiceNode> getServices();
}
