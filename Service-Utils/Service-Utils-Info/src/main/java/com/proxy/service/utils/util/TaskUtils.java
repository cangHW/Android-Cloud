package com.proxy.service.utils.util;

import com.proxy.service.api.task.ITask;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.task.TaskCallable;
import com.proxy.service.api.task.TaskHelper;
import com.proxy.service.utils.lock.TaskLock;
import com.proxy.service.utils.pool.thread.task.TaskImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/08/09  6:14 PM
 */
public class TaskUtils {

    /**
     * 根据条件创建任务辅助器
     *
     * @param task  : 任务体
     * @param locks : 任务锁
     * @return 任务辅助对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 6:16 PM
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <RESPONSE, RESULT> TaskHelper<RESULT> createTaskHelper(TaskCallable<RESPONSE, RESULT> task, TaskLock<RESPONSE>... locks) {
        ITask<RESPONSE>[] iTasks = new ITask[locks.length];
        for (int i = 0; i < locks.length; i++) {
            TaskLock<RESPONSE> lock = locks[i];
            iTasks[i] = new TaskImpl<>(lock.isSuccess(), lock.getResponse(), lock.getThrowable());
        }
        return new TaskHelper<>(task, iTasks);
    }

    /**
     * 根据条件创建任务辅助器
     *
     * @param task : 任务体
     * @return 任务辅助对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 6:16 PM
     */
    public static <RESULT> TaskHelper<RESULT> createTaskHelper(Task<RESULT> task) {
        return new TaskHelper<>(task);
    }

}
