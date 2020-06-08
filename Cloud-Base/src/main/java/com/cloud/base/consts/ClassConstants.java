package com.cloud.base.consts;

import com.cloud.base.base.AbstractServiceCache;
import com.cloud.base.base.BaseService;
import com.cloud.base.node.ServiceNode;

/**
 * @author: cangHX
 * on 2020/06/04  18:36
 * <p>
 * 辅助类信息
 */
public class ClassConstants {

    /**
     * 编译器生成的辅助文件所在包的路径
     */
    public static final String PACKAGE_SERVICES_CACHE = "com.cloud.service";
    /**
     * 编译器生成的辅助文件类名前缀
     */
    public static final String CLASS_PREFIX = "Cloud$$Service$$Cache";

    /**
     * 编译器生成的辅助文件父类路径
     */
    public static final String SUPPER_CLASS_PATH = AbstractServiceCache.class.getCanonicalName();

    /**
     * 编译器生成的辅助文件所需实现方法
     */
    public static final String SUPPER_CLASS_METHOD_NAME = "getServices";

    /**
     * 编译器生成的辅助文件所需参数路径
     */
    public static final String PARAM_SERVICE_NODE_CLASS_PATH = ServiceNode.class.getCanonicalName();
}
