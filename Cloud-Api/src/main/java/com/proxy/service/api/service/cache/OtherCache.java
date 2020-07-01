package com.proxy.service.api.service.cache;

import androidx.annotation.NonNull;

import com.proxy.service.node.OtherNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/08  17:46
 * <p>
 * 服务类仓库
 */
public class OtherCache {

    /**
     * 服务集合类
     */
    private static final List<OtherNode> SERVICES = new ArrayList<>();

    /**
     * 获取数据量
     *
     * @return 返回数量
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-08 18:42
     */
    public static int size() {
        return SERVICES.size();
    }

    /**
     * 注册
     *
     * @param services : 数据集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-08 18:17
     */
    public synchronized static void addAll(@NonNull List<OtherNode> services) {
        SERVICES.addAll(services);
    }

    /**
     * 获取
     *
     * @return 获取全部数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-08 18:21
     */
    public synchronized static List<OtherNode> getAll() {
        return new ArrayList<>(SERVICES);
    }

}
