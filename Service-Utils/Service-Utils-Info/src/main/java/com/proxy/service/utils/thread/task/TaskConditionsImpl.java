package com.proxy.service.utils.thread.task;

import com.proxy.service.api.task.ITaskConditions;
import com.proxy.service.api.task.ITaskFunction;
import com.proxy.service.utils.lock.TaskLock;
import com.proxy.service.utils.thread.enums.TaskThreadEnum;

/**
 * @author : cangHX
 * on 2020/08/07  11:24 PM
 */
public class TaskConditionsImpl<RESPONSE> extends TaskFunctionImpl<RESPONSE> implements ITaskConditions<RESPONSE> {

    public TaskConditionsImpl(TaskThreadEnum threadEnum, TaskLock<RESPONSE> lock) {
        super(threadEnum, lock);
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
        ITaskFunction<RESPONSE> taskFunction = new TaskFunctionImpl<>(TaskThreadEnum.MAIN, mLock);
        TASK_FUNCTIONS.add(taskFunction);
        return taskFunction;
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
        ITaskFunction<RESPONSE> taskFunction = new TaskFunctionImpl<>(TaskThreadEnum.WORK, mLock);
        TASK_FUNCTIONS.add(taskFunction);
        return taskFunction;
    }

}
