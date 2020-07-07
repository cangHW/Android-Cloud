package com.proxy.service.ui.info;

import com.proxy.service.annotations.CloudService;
import com.proxy.service.api.callback.CloudUiFieldCheckErrorCallback;
import com.proxy.service.api.services.CloudUiFieldCheckService;
import com.proxy.service.api.tag.CloudServiceTagUi;

/**
 * @author: cangHX
 * on 2020/07/07  19:12
 */
@CloudService(serviceTag = CloudServiceTagUi.UI_FIELD_CHECK)
public class CloudUiFieldCheckServiceImpl implements CloudUiFieldCheckService {
    /**
     * 设置全局检查失败回调
     *
     * @param callback : 检查失败回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:18
     */
    @Override
    public void setGlobalErrorToastCallback(CloudUiFieldCheckErrorCallback callback) {

    }

    /**
     * 设置临时检查失败回调
     *
     * @param callback : 检查失败回调
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 17:18
     */
    @Override
    public void setErrorToastCallback(CloudUiFieldCheckErrorCallback callback) {

    }

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
    @Override
    public CloudUiFieldCheckService of(String markId, String s) {
        return null;
    }

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
    @Override
    public CloudUiFieldCheckService of(String markId, double d) {
        return null;
    }

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
    @Override
    public CloudUiFieldCheckService of(String markId, float f) {
        return null;
    }

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
    @Override
    public CloudUiFieldCheckService of(String markId, int i) {
        return null;
    }

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
    @Override
    public CloudUiFieldCheckService of(String markId, long l) {
        return null;
    }

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
    @Override
    public CloudUiFieldCheckService of(String markId, boolean b) {
        return null;
    }

    /**
     * 检测成功执行，主线程
     *
     * @param runnable : 运行体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 18:45
     */
    @Override
    public void runUi(Runnable runnable) {

    }

    /**
     * 检测成功执行，子线程
     *
     * @param runnable : 运行体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-07 18:45
     */
    @Override
    public void runBg(Runnable runnable) {

    }
}
