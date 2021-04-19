package com.proxy.service.utils.util;

import com.proxy.service.api.task.ITask;
import com.proxy.service.api.task.ITaskFunction;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.task.TaskCallable;
import com.proxy.service.api.task.TaskHelper;
import com.proxy.service.utils.lock.TaskLock;
import com.proxy.service.utils.pool.thread.ThreadManager;
import com.proxy.service.utils.pool.thread.enums.TaskThreadEnum;
import com.proxy.service.utils.pool.thread.task.TaskImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : cangHX
 * on 2020/08/09  6:14 PM
 */
public class TaskUtils {

    /**
     * 根据条件创建任务体数组
     *
     * @param locks : 任务锁
     * @return 任务体数组
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 6:16 PM
     */
    @SuppressWarnings("unchecked")
    public static <RESPONSE> ITask<RESPONSE>[] createTasks(TaskThreadEnum threadEnum, TaskLock<RESPONSE> locks) {
        ITask<RESPONSE>[] iTasks = new ITask[locks.getResponseList().size()];
        for (int i = 0; i < iTasks.length; i++) {
            TaskLock.Response<RESPONSE> response = locks.getResponseList().get(i);
            iTasks[i] = new TaskImpl<>(response.isSuccess, response.response, response.throwable, threadEnum, new TaskLock<RESPONSE>());
        }
        return iTasks;
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


    /**
     * 满足条件后继续执行
     *
     * @param isFinishAll : 是否全部完成 true，必须全部完成，false 非必须
     * @param lock        : 任务锁
     * @param functions   : 前置任务
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/10 6:45 PM
     */

    public static <RESPONSE> TaskLock<Object> when(boolean isFinishAll, final TaskLock<RESPONSE> lock, final ITaskFunction<?>... functions) {
        return when(isFinishAll, -1, null, lock, functions);
    }

    /**
     * 满足条件或到达最大时间后继续执行
     *
     * @param isFinishAll : 是否全部完成 true，必须全部完成，false 非必须
     * @param timeOut     : 超时时间
     * @param unit        : 时间格式
     * @param lock        : 任务锁
     * @param functions   : 前置任务
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/10 6:45 PM
     */
    public static <RESPONSE> TaskLock<Object> when(boolean isFinishAll, long timeOut, TimeUnit unit, TaskLock<RESPONSE> lock, ITaskFunction<?>... functions) {
        final TaskLock<Object> taskLock = new TaskLock<>();
        taskLock.lock();

        final List<TaskLock.Response<Object>> responses = new ArrayList<>();
        Utils.fill(responses, null, functions.length);
        taskLock.setResponseList(responses);

        TaskState taskState = new TaskState();
        TaskUtils taskUtils = new TaskUtils();
        if (timeOut > 0) {
            taskUtils.checkTimeOutState(taskState, taskLock, timeOut, unit);
        }
        taskUtils.checkSharedLockState(taskState, lock, taskLock);
        taskUtils.checkTaskFinishState(isFinishAll, taskState, taskLock, functions);
        return taskLock;
    }

    private static class TaskState {
        private final AtomicBoolean isHasSharedLock = new AtomicBoolean(false);
        private final AtomicBoolean isTasksFinish = new AtomicBoolean(false);
        private final AtomicBoolean isTimeOut = new AtomicBoolean(false);
    }

    /**
     * 检查超时状态
     */
    private void checkTimeOutState(final TaskState taskState, final TaskLock<Object> taskLock, long timeOut, TimeUnit unit) {
        ThreadManager.postScheduled(new Runnable() {
            @Override
            public void run() {
                synchronized (taskState) {
                    if (taskState.isHasSharedLock.get() && taskState.isTasksFinish.get()) {
                        return;
                    }
                    taskState.isTimeOut.set(true);
                    sortResponse(taskLock);
                    taskLock.unLock();
                }
            }
        }, timeOut, unit);
    }

    /**
     * 检查任务执行状态
     */
    @SuppressWarnings("unchecked")
    private void checkTaskFinishState(final boolean isFinishAll, final TaskState taskState, final TaskLock<Object> taskLock, final ITaskFunction<?>... functions) {
        final AtomicInteger responseCount = new AtomicInteger(0);
        final ArrayList<String> responseIndex = new ArrayList<>(functions.length);
        Utils.fill(responseIndex, null, functions.length);
        for (int i = 0; i < functions.length; i++) {
            ITaskFunction<Object> taskFunction = (ITaskFunction<Object>) functions[i];
            final int finalI = i;
            taskFunction.call(new TaskCallable<Object, Object>() {
                @Override
                public Object then(ITask<Object>[] iTasks) {
                    synchronized (taskState) {
                        if (taskState.isTimeOut.get()) {
                            return null;
                        }
                        if (taskState.isTasksFinish.get()) {
                            return null;
                        }
                        int count = iTasks == null ? 0 : iTasks.length;
                        List<TaskLock.Response<Object>> list = new ArrayList<>(count);
                        for (int k = 0; k < count; k++) {
                            ITask<Object> iTask = iTasks[k];

                            TaskLock.Response<Object> response = new TaskLock.Response<>();
                            response.isSuccess = iTask.isSuccess();
                            response.response = iTask.getResponse();
                            response.throwable = iTask.getThrowable();
                            list.add(response);
                        }

                        if (list.size() > 0) {
                            int index = -1;
                            for (int i1 = 0; i1 <= finalI; i1++) {
                                if (responseIndex.get(i1) == null) {
                                    index++;
                                }
                            }
                            Utils.fill(taskLock.getResponseList(), list, index, null);
                            responseIndex.set(finalI, "");
                        }

                        if (!isFinishAll) {
                            taskState.isTasksFinish.set(true);
                            if (taskState.isHasSharedLock.get()) {
                                sortResponse(taskLock);
                                taskLock.unLock();
                            }
                            return null;
                        }
                        if (responseCount.incrementAndGet() == functions.length) {
                            taskState.isTasksFinish.set(true);
                            if (taskState.isHasSharedLock.get()) {
                                sortResponse(taskLock);
                                taskLock.unLock();
                            }
                        }
                    }
                    return null;
                }
            });
        }
    }

    /**
     * 检查共享锁状态
     */
    @SuppressWarnings("unchecked")
    private <RESPONSE> void checkSharedLockState(final TaskState taskState, final TaskLock<RESPONSE> lock, final TaskLock<Object> taskLock) {
        lock.trySharedLock(new Runnable() {
            @Override
            public void run() {
                synchronized (taskState) {
                    if (taskState.isTimeOut.get()) {
                        return;
                    }
                    taskState.isHasSharedLock.set(true);
                    List<TaskLock.Response<RESPONSE>> list = lock.getResponseList();
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            TaskLock.Response<Object> response = (TaskLock.Response<Object>) list.get(i);
                            taskLock.getResponseList().add(i, response);
                        }
                    }
                    if (taskState.isTasksFinish.get()) {
                        sortResponse(taskLock);
                        taskLock.unLock();
                    }
                }
            }
        });
    }

    /**
     * 返回值排序
     */
    private void sortResponse(TaskLock<Object> taskLock) {
        List<TaskLock.Response<Object>> responses = taskLock.getResponseList();
        List<TaskLock.Response<Object>> list = new ArrayList<>();
        for (TaskLock.Response<Object> response : responses) {
            if (response == null) {
                continue;
            }
            list.add(response);
        }
        taskLock.setResponseList(list);
    }
}
