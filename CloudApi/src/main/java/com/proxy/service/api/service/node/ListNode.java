package com.proxy.service.api.service.node;

import com.proxy.service.api.service.listener.Converter;
import com.proxy.service.base.BaseService;

import java.util.ArrayList;

/**
 * 自定义数据结构
 *
 * @author: cangHX
 * on 2020/05/22  14:47
 */
public class ListNode {

    private final ArrayList<Node> nodes = new ArrayList<>();

    /**
     * 获取数据数量
     *
     * @return 返回数据数量
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-05-22 17:51
     */
    public int size() {
        return nodes.size();
    }

    /**
     * 添加数据
     *
     * @param uuid      : id
     * @param converter : 转换器
     * @return 是否成功，true 成功，false 失败(数据重复)
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-05-22 17:45
     */
    public synchronized <T extends BaseService> boolean add(String uuid, Converter<T> converter) {
        nodes.add(new Node(uuid, converter));
        return true;
    }

    /**
     * 移除数据
     *
     * @param uuid : id
     * @return 是否成功，true 成功，false 失败(对应数据不存在)
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-05-22 17:48
     */
    @SuppressWarnings("UnusedReturnValue")
    public synchronized boolean remove(String uuid) {
        int count = 0;
        ArrayList<Node> list = list();
        for (Node node : list) {
            if (node.getUuid().equals(uuid)) {
                nodes.remove(node);
                count++;
            }
        }
        return count > 0;
    }

    /**
     * 移除数据
     *
     * @param node : 准备移除的数据
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-05-22 17:48
     */
    public synchronized void remove(Node node) {
        nodes.remove(node);
    }

    /**
     * 生成快照数组
     *
     * @return 快照数组
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-05-22 17:53
     */
    public ArrayList<Node> list() {
        return new ArrayList<>(nodes);
    }
}
