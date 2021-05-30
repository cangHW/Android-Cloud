package com.proxy.service.api.task;

import androidx.annotation.NonNull;

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
     * 等待执行，工作线程执行，后续仍然为工作线程
     *
     * @param callback : 回调接口，返回 true 继续向下执行，false 任务结束
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:49 PM
     */
    ITaskConditions<RESPONSE> await(Callable<Boolean> callback);

    /**
     * 等待执行，工作线程执行，后续仍然为工作线程
     *
     * @param timeOut : 等待时间
     * @param unit    : 时间格式
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:49 PM
     */
    ITaskConditions<RESPONSE> await(long timeOut, TimeUnit unit);

    /**
     * 当前置任务全部返回后继续执行，当前线程执行，后续切换到工作线程
     *
     * @param functions : 前置任务
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:50 PM
     */
    @NonNull
    ITaskConditions<Object> whenAll(@NonNull ITaskFunction<?>... functions);

    /**
     * 当前置任务全部返回，或达到最大等待时间后继续执行，当前线程执行，后续切换到工作线程
     *
     * @param timeOut   : 等待时间
     * @param unit      : 时间格式
     * @param functions : 前置任务
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:52 PM
     */
    @NonNull
    ITaskConditions<Object> whenAll(long timeOut, TimeUnit unit, @NonNull ITaskFunction<?>... functions);

    /**
     * 当前置任务其中一个返回后继续执行，当前线程执行，后续切换到工作线程
     *
     * @param functions : 前置任务
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:50 PM
     */
    @NonNull
    ITaskConditions<Object> whenAny(@NonNull ITaskFunction<?>... functions);

    /**
     * 当前置任务其中一个返回，或达到最大等待时间后继续执行，当前线程执行，后续切换到工作线程
     *
     * @param timeOut   : 等待时间
     * @param unit      : 时间格式
     * @param functions : 前置任务
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:52 PM
     */
    @NonNull
    ITaskConditions<Object> whenAny(long timeOut, TimeUnit unit, @NonNull ITaskFunction<?>... functions);

    /**
     * 循环执行，当前线程执行，后续切换到工作线程
     *
     * @param callable : 回调接口，返回 true 继续执行，false 跳出循环
     * @param task     : 任务体
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:53 PM
     */
    @NonNull
    <RESULT> ITaskConditions<RESULT> continueWhile(@NonNull Callable<Boolean> callable, @NonNull TaskCallable<RESPONSE, RESULT> task);

    /**
     * 在当前线程执行，接收所有数据，后续切换到工作线程
     *
     * @param task : 任务体
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:09 PM
     */
    @NonNull
    <RESULT> ITaskConditions<RESULT> call(@NonNull TaskCallable<RESPONSE, RESULT> task);

    /**
     * 在当前线程执行，接收单个数据，后续切换到工作线程
     *
     * @param task : 任务体
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:09 PM
     */
    @NonNull
    <RESULT> ITaskConditions<RESULT> call(@NonNull TaskCallableOnce<RESPONSE, RESULT> task);

    /**
     * 取消当前任务
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:10 PM
     */
    void cancel();
}
