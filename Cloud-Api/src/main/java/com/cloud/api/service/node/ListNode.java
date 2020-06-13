package com.cloud.api.service.node;

import com.cloud.api.service.listener.Converter;

import java.util.ArrayList;

/**
 * 自定义数据结构，效果：顺序、值唯一
 *
 * @author: cangHX
 * on 2020/05/22  14:47
 */
public class ListNode {

    private ArrayList<Node> nodes = new ArrayList<>();

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
     * @param uuid      : 唯一id
     * @param converter : 转换器
     * @return 是否成功，true 成功，false 失败(数据重复)
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-05-22 17:45
     */
    public synchronized boolean add(String uuid, Converter converter) {
        if (contains(uuid)) {
            return false;
        }
        nodes.add(new Node(uuid, converter));
        return true;
    }

    /**
     * 移除数据
     *
     * @param uuid : 唯一id
     * @return 是否成功，true 成功，false 失败(对应数据不存在)
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-05-22 17:48
     */
    public synchronized boolean remove(String uuid) {
        for (Node node : nodes) {
            if (node.getUuid().equals(uuid)) {
                nodes.remove(node);
                return true;
            }
        }
        return false;
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

    /**
     * 当前数据是否已存在
     *
     * @param uuid : 唯一ID
     * @return 是否存在，true 存在，false 不存在
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-05-22 17:47
     */
    private boolean contains(String uuid) {
        for (Node node : nodes) {
            if (node.getUuid().equals(uuid)) {
                return true;
            }
        }
        return false;
    }
}
