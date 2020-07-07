package com.proxy.service.api.services;

import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;
import com.proxy.service.base.BaseService;

/**
 * @author: cangHX
 * on 2020/07/07  16:57
 * <p>
 * 提供变量信息检查以及提示的服务
 */
public interface CloudUiFieldCheckService extends BaseService {

    /**
     * 设置全局检查失败回调
     *
     * @param callback : 检查失败回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:18
     */
    void setGlobalErrorToastCallback(CloudUiFieldCheckErrorCallback callback);

    /**
     * 设置临时检查失败回调
     *
     * @param callback : 检查失败回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:18
     */
    void setErrorToastCallback(CloudUiFieldCheckErrorCallback callback);

    /**
     * 添加一个待检测的 String 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param s      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    CloudUiFieldCheckService of(String markId, String s);

    /**
     * 添加一个待检测的 double 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param d      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    CloudUiFieldCheckService of(String markId, double d);

    /**
     * 添加一个待检测的 float 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param f      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    CloudUiFieldCheckService of(String markId, float f);

    /**
     * 添加一个待检测的 int 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param i      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    CloudUiFieldCheckService of(String markId, int i);

    /**
     * 添加一个待检测的 long 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param l      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    CloudUiFieldCheckService of(String markId, long l);

    /**
     * 添加一个待检测的 boolean 数据
     *
     * @param markId : 标记id，标记当前变量
     * @param b      : 内容
     * @return 当前对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:19
     */
    CloudUiFieldCheckService of(String markId, boolean b);

    /**
     * 检测成功执行，主线程
     *
     * @param runnable : 运行体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 18:45
     */
    void runUi(Runnable runnable);

    /**
     * 检测成功执行，子线程
     *
     * @param runnable : 运行体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 18:45
     */
    void runBg(Runnable runnable);
}
