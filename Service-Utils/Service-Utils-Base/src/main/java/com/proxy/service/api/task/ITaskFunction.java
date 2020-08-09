package com.proxy.service.api.task;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 任务功能相关
 *
 * @author : cangHX
 * on 2020/08/07  10:15 PM
 */
public interface ITaskFunction<RESPONSE> {

    /**
     * 当前置任务全部返回后继续执行
     *
     * @param functions : 前置任务
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:50 PM
     */
    ITaskConditions<RESPONSE> whenAll(ITaskFunction<?>... functions);

    /**
     * 当前置任务全部返回，或达到最大等待时间后继续执行
     *
     * @param timeOut  : 等待时间
     * @param unit     : 时间格式
     * @param services : 前置任务
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:52 PM
     */
    ITaskConditions<RESPONSE> whenAll(long timeOut, TimeUnit unit, ITaskFunction<?>... functions);

    /**
     * 当前置任务其中一个返回后继续执行
     *
     * @param services : 前置任务
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:50 PM
     */
    ITaskConditions<RESPONSE> whenAny(ITaskFunction<?>... functions);

    /**
     * 当前置任务其中一个返回，或达到最大等待时间后继续执行
     *
     * @param timeOut  : 等待时间
     * @param unit     : 时间格式
     * @param services : 前置任务
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:52 PM
     */
    ITaskConditions<RESPONSE> whenAny(long timeOut, TimeUnit unit, ITaskFunction<?>... functions);

    /**
     * 循环执行
     *
     * @param callable : 回调接口，返回 true 继续执行，false 跳出循环
     * @param task     : 任务体
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:53 PM
     */
    <RESULT> ITaskConditions<RESULT> continueWhile(Callable<Boolean> callable, TaskCallable<RESPONSE, RESULT> task);

    /**
     * 运行任务
     *
     * @param task : 任务体
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:09 PM
     */
    <RESULT> ITaskConditions<RESULT> call(TaskCallable<RESPONSE, RESULT> task);

    /**
     * 取消当前任务
     *
     * @return true 取消成功，false 取消失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:10 PM
     */
    boolean cancel();
}
