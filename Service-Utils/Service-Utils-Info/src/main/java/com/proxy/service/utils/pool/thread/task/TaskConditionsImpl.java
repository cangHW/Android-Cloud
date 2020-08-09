package com.proxy.service.utils.pool.thread.task;

import com.proxy.service.api.task.ITaskConditions;
import com.proxy.service.api.task.ITaskFunction;
import com.proxy.service.api.task.TaskCallable;
import com.proxy.service.api.task.TaskHelper;
import com.proxy.service.utils.lock.TaskLock;
import com.proxy.service.utils.pool.thread.enums.TaskThreadEnum;
import com.proxy.service.utils.util.TaskUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author : cangHX
 * on 2020/08/07  11:24 PM
 */
public class TaskConditionsImpl<RESPONSE> implements ITaskConditions<RESPONSE> {

    private final TaskThreadEnum mThreadEnum;
    private final TaskLock<RESPONSE> mLock;

    public TaskConditionsImpl(TaskThreadEnum threadEnum, TaskLock<RESPONSE> lock) {
        this.mThreadEnum = threadEnum;
        this.mLock = lock;
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
     * @param functions : 前置任务
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:50 PM
     */
    @Override
    public ITaskConditions<RESPONSE> whenAll(ITaskFunction<?>... functions) {
        return null;
    }

    /**
     * 当前置任务全部返回，或达到最大等待时间后继续执行
     *
     * @param timeOut  : 等待时间
     * @param unit     : 时间格式
     * @param functions : 前置任务
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:52 PM
     */
    @Override
    public ITaskConditions<RESPONSE> whenAll(long timeOut, TimeUnit unit, ITaskFunction<?>... functions) {
        return null;
    }

    /**
     * 当前置任务其中一个返回后继续执行
     *
     * @param functions : 前置任务
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:50 PM
     */
    @Override
    public ITaskConditions<RESPONSE> whenAny(ITaskFunction<?>... functions) {
        return null;
    }

    /**
     * 当前置任务其中一个返回，或达到最大等待时间后继续执行
     *
     * @param timeOut  : 等待时间
     * @param unit     : 时间格式
     * @param functions : 前置任务
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:52 PM
     */
    @Override
    public ITaskConditions<RESPONSE> whenAny(long timeOut, TimeUnit unit, ITaskFunction<?>... functions) {
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
    public <RESULT> ITaskConditions<RESULT> call(final TaskCallable<RESPONSE, RESULT> task) {
        final TaskLock<RESULT> taskLock = new TaskLock<>();
        taskLock.lock();
        mLock.trySharedLock(new Runnable() {
            @Override
            public void run() {
                TaskHelper<RESULT> taskHelper = TaskUtils.createTaskHelper(task, mLock);
                taskHelper.setCallback(new TaskHelper.TaskCallback<RESULT>() {
                    @Override
                    public void success(RESULT t) {
                        taskLock.setResponse(t);
                        taskLock.unLock();
                    }

                    @Override
                    public void failed(Throwable throwable) {
                        taskLock.setThrowable(throwable);
                        taskLock.unLock();
                    }
                });
                mThreadEnum.postTask(taskHelper);
            }
        });
        return new TaskConditionsImpl<>(mThreadEnum, taskLock);
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
