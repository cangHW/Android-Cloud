package com.proxy.service.api.task;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 任务功能与条件设置
 *
 * @author : cangHX
 * on 2020/08/09  2:54 PM
 */
public interface ITaskConditions<RESPONSE> extends ITaskFunction<RESPONSE> {

    /**
     * 切换线程为主线程
     *
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:47 PM
     */
    ITaskFunction<RESPONSE> mainThread();

    /**
     * 切换线程为工作线程
     *
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:48 PM
     */
    ITaskFunction<RESPONSE> workThread();

}
