package com.proxy.service.api.task;

/**
 * 任务功能、条件设置与返回值
 *
 * @author : cangHX
 * on 2020/08/07  11:19 PM
 */
public interface ITask<RESPONSE> extends ITaskConditions<RESPONSE> {

    /**
     * 是否执行成功
     *
     * @return true 成功，false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:22 PM
     */
    boolean isSuccess();

    /**
     * 获取返回值
     *
     * @return 返回值
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:10 PM
     */
    RESPONSE getResponse();

    /**
     * 获取异常信息
     *
     * @return 异常信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:10 PM
     */
    Throwable getThrowable();

}
