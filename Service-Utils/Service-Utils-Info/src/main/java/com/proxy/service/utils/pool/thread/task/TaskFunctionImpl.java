package com.proxy.service.utils.pool.thread.task;

import com.proxy.service.api.task.ITaskConditions;
import com.proxy.service.api.task.ITaskFunction;
import com.proxy.service.api.task.TaskCallable;
import com.proxy.service.utils.lock.TaskLock;
import com.proxy.service.utils.pool.thread.enums.TaskThreadEnum;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author : cangHX
 * on 2020/08/07  11:23 PM
 */
public class TaskFunctionImpl implements ITaskFunction {

    private final TaskThreadEnum threadEnum;
    private final TaskLock lock;

    public TaskFunctionImpl(TaskThreadEnum threadEnum, TaskLock lock){
        this.threadEnum = threadEnum;
        this.lock = lock;
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
    public ITaskConditions whenAll(ITaskFunction[] services) {
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
    public ITaskConditions whenAll(long timeOut, TimeUnit unit, ITaskFunction[] services) {
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
    public ITaskConditions whenAny(ITaskFunction[] services) {
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
    public ITaskConditions whenAny(long timeOut, TimeUnit unit, ITaskFunction[] services) {
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
    public ITaskConditions continueWhile(Callable callable, TaskCallable task) {
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
    public ITaskConditions call(TaskCallable task) {
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
