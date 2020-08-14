package com.proxy.service.ui.fieldcheck.node;

import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;

/**
 * @author: cangHX
 * on 2020/07/08  10:02
 */
public abstract class BaseFieldCheckNode {

    /**
     * 标记id，标记当前变量
     */
    public String markId;

    /**
     * 校验失败后的错误信息
     */
    public String message;

    /**
     * 检测是否符合要求
     *
     * @param object   : 数据包装对象
     * @param callback : 变量信息检查回调接口
     * @return true 有错误，false 没有错误
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-08 15:26
     */
    public abstract boolean isHasError(Object object, CloudUiFieldCheckErrorCallback callback);
}
