package com.proxy.service.utils.pool.thread.task;

import com.proxy.service.api.task.ITask;
import com.proxy.service.api.task.ITaskConditions;
import com.proxy.service.api.task.ITaskFunction;
import com.proxy.service.api.task.TaskCallable;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author : cangHX
 * on 2020/08/09  4:31 PM
 */
public class TaskImpl<RESPONSE> implements ITask<RESPONSE> {

    private boolean isSuccess;
    private RESPONSE mResponse;
    private Throwable mThrowable;

    public TaskImpl(boolean isSuccess, RESPONSE response, Throwable throwable) {
        this.isSuccess = isSuccess;
        this.mResponse = response;
        this.mThrowable = throwable;
    }

    /**
     * 是否执行成功
     *
     * @return true 成功，false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:22 PM
     */
    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * 获取返回值
     *
     * @return 返回值
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:10 PM
     */
    @Override
    public RESPONSE getResponse() {
        return mResponse;
    }

    /**
     * 获取异常信息
     *
     * @return 异常信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:10 PM
     */
    @Override
    public Throwable getThrowable() {
        return mThrowable;
    }

    /**
     * 切换线程为主线程
     *
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:47 PM
     */
    @Override
    public ITaskFunction<RESPONSE> mainThread() {
        return null;
    }

    /**
     * 切换线程为工作线程
     *
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:48 PM
     */
    @Override
    public ITaskFunction<RESPONSE> workThread() {
        return null;
    }

    /**
     * 等待执行
     *
     * @param callback : 回调接口，返回 true 继续向下执行，false 任务结束
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:49 PM
     */
    @Override
    public ITaskFunction<RESPONSE> await(Callable<Boolean> callback) {
        return null;
    }

    /**
     * 等待执行
     *
     * @param timeOut : 等待时间
     * @param unit    : 时间格式
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:49 PM
     */
    @Override
    public ITaskFunction<RESPONSE> await(long timeOut, TimeUnit unit) {
        return null;
    }

    /**
     * 当前置任务全部返回后继续执行
     *
     * @param services : 前置任务
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:50 PM
     */
    @Override
    public ITaskConditions<RESPONSE> whenAll(ITaskFunction<?>... services) {
        return null;
    }

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
    @Override
    public ITaskConditions<RESPONSE> whenAll(long timeOut, TimeUnit unit, ITaskFunction<?>... services) {
        return null;
    }

    /**
     * 当前置任务其中一个返回后继续执行
     *
     * @param services : 前置任务
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:50 PM
     */
    @Override
    public ITaskConditions<RESPONSE> whenAny(ITaskFunction<?>... services) {
        return null;
    }

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
    @Override
    public ITaskConditions<RESPONSE> whenAny(long timeOut, TimeUnit unit, ITaskFunction<?>... services) {
        return null;
    }

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
    @Override
    public <RESULT> ITaskConditions<RESULT> continueWhile(Callable<Boolean> callable, TaskCallable<RESPONSE, RESULT> task) {
        return null;
    }

    /**
     * 运行任务
     *
     * @param task : 任务体
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:09 PM
     */
    @Override
    public <RESULT> ITaskConditions<RESULT> call(TaskCallable<RESPONSE, RESULT> task) {
        return null;
    }

    /**
     * 取消当前任务
     *
     * @return true 取消成功，false 取消失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:10 PM
     */
    @Override
    public boolean cancel() {
        return false;
    }
}
