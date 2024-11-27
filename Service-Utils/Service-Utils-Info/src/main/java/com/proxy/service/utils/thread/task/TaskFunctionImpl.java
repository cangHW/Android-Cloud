package com.proxy.service.utils.thread.task;

import androidx.annotation.NonNull;

import com.proxy.service.api.task.ITask;
import com.proxy.service.api.task.ITaskConditions;
import com.proxy.service.api.task.ITaskFunction;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.task.TaskCallable;
import com.proxy.service.api.task.TaskCallableOnce;
import com.proxy.service.api.task.TaskHelper;
import com.proxy.service.api.log.Logger;
import com.proxy.service.utils.lock.TaskLock;
import com.proxy.service.utils.thread.enums.TaskThreadEnum;
import com.proxy.service.utils.util.TaskUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author : cangHX
 * on 2020/08/07  11:23 PM
 */
public class TaskFunctionImpl<RESPONSE> implements ITaskFunction<RESPONSE> {

    protected final List<Future<?>> TASK_HELPERS = new ArrayList<>();
    protected final List<ITaskFunction<?>> TASK_FUNCTIONS = new ArrayList<>();
    protected final TaskThreadEnum mThreadEnum;
    protected final TaskLock<RESPONSE> mLock;

    public TaskFunctionImpl(TaskThreadEnum threadEnum, TaskLock<RESPONSE> lock) {
        this.mThreadEnum = threadEnum;
        this.mLock = lock;
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
    public ITaskConditions<RESPONSE> await(final Callable<Boolean> callback) {
        final TaskLock<RESPONSE> taskLock = new TaskLock<>();
        taskLock.lock();

        final TaskHelper<Object> taskHelper = new TaskHelper<>(new Task<Object>() {
            @Override
            public Object call() throws Exception {
                if (callback.call()) {
                    taskLock.unLock();
                } else {
                    taskLock.cancel();
                }
                return null;
            }
        });
        taskHelper.setTaskCallback(new TaskHelper.TaskCallback<Object>() {
            @Override
            public void success(Object response) {
            }

            @Override
            public void failed(Throwable throwable) {
                taskLock.cancel();
            }
        });
        taskHelper.setCancelCallback(new TaskHelper.TaskCancelCallback() {
            @Override
            public void cancel() {
                taskLock.cancel();
            }
        });
        TASK_HELPERS.add(taskHelper);

        mLock.trySharedLock(new Runnable() {
            @Override
            public void run() {
                mThreadEnum.postTask(taskHelper);
            }
        });

        ITaskConditions<RESPONSE> taskConditions = new TaskConditionsImpl<>(mThreadEnum, taskLock);
        TASK_FUNCTIONS.add(taskConditions);
        return taskConditions;
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
    public ITaskConditions<RESPONSE> await(final long timeOut, final TimeUnit unit) {
        final TaskLock<RESPONSE> taskLock = new TaskLock<>();
        taskLock.lock();

        final TaskHelper<Object> taskHelper = new TaskHelper<>(new Task<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(unit.toMillis(timeOut));
                taskLock.unLock();
                return null;
            }
        });
        taskHelper.setTaskCallback(new TaskHelper.TaskCallback<Object>() {
            @Override
            public void success(Object response) {
            }

            @Override
            public void failed(Throwable throwable) {
                taskLock.cancel();
            }
        });
        taskHelper.setCancelCallback(new TaskHelper.TaskCancelCallback() {
            @Override
            public void cancel() {
                taskLock.cancel();
            }
        });
        TASK_HELPERS.add(taskHelper);

        mLock.trySharedLock(new Runnable() {
            @Override
            public void run() {
                mThreadEnum.postTask(taskHelper);
            }
        });

        ITaskConditions<RESPONSE> taskConditions = new TaskConditionsImpl<>(mThreadEnum, taskLock);
        TASK_FUNCTIONS.add(taskConditions);
        return taskConditions;
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
    @NonNull
    @Override
    public ITaskConditions<Object> whenAll(@NonNull final ITaskFunction<?>... functions) {
        TASK_FUNCTIONS.addAll(Arrays.asList(functions));
        ITaskConditions<Object> taskConditions = new TaskConditionsImpl<>(mThreadEnum, TaskUtils.when(true, mLock, functions));
        TASK_FUNCTIONS.add(taskConditions);
        return taskConditions;
    }

    /**
     * 当前置任务全部返回，或达到最大等待时间后继续执行
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
    @Override
    public ITaskConditions<Object> whenAll(long timeOut, TimeUnit unit, @NonNull ITaskFunction<?>... functions) {
        TASK_FUNCTIONS.addAll(Arrays.asList(functions));
        ITaskConditions<Object> taskConditions = new TaskConditionsImpl<>(mThreadEnum, TaskUtils.when(true, timeOut, unit, mLock, functions));
        TASK_FUNCTIONS.add(taskConditions);
        return taskConditions;
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
    @NonNull
    @Override
    public ITaskConditions<Object> whenAny(@NonNull ITaskFunction<?>... functions) {
        TASK_FUNCTIONS.addAll(Arrays.asList(functions));
        ITaskConditions<Object> taskConditions = new TaskConditionsImpl<>(mThreadEnum, TaskUtils.when(false, mLock, functions));
        TASK_FUNCTIONS.add(taskConditions);
        return taskConditions;
    }

    /**
     * 当前置任务其中一个返回，或达到最大等待时间后继续执行
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
    @Override
    public ITaskConditions<Object> whenAny(long timeOut, TimeUnit unit, @NonNull ITaskFunction<?>... functions) {
        TASK_FUNCTIONS.addAll(Arrays.asList(functions));
        ITaskConditions<Object> taskConditions = new TaskConditionsImpl<>(mThreadEnum, TaskUtils.when(false, timeOut, unit, mLock, functions));
        TASK_FUNCTIONS.add(taskConditions);
        return taskConditions;
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
    @NonNull
    @Override
    public <RESULT> ITaskConditions<RESULT> continueWhile(@NonNull final Callable<Boolean> callable, @NonNull final TaskCallable<RESPONSE, RESULT> task) {
        final TaskLock<RESULT> taskLock = new TaskLock<>();
        taskLock.lock();

        final ArrayList<ITask<RESPONSE>[]> list = new ArrayList<>(1);
        final TaskHelper<RESULT> taskHelper = new TaskHelper<>(new Task<RESULT>() {
            @Override
            public RESULT call() throws Exception {
                ITask<RESPONSE>[] iTasks = list.get(0);
                list.clear();
                while (callable.call()) {
                    RESULT result = task.then(iTasks);
                    if (result == null) {
                        Logger.Warning("The result is null, but result in continue mode can not be null, so missing this result. with task : " + task);
                        continue;
                    }
                    TaskLock.Response<RESULT> response = new TaskLock.Response<>();
                    response.isSuccess = true;
                    response.response = result;
                    taskLock.getResponseList().add(response);
                }
                taskLock.unLock();
                return null;
            }
        });
        taskHelper.setTaskCallback(new TaskHelper.TaskCallback<RESULT>() {
            @Override
            public void success(RESULT response) {
            }

            @Override
            public void failed(Throwable throwable) {
                if (taskLock.getResponseList().size() == 0) {
                    taskLock.setThrowable(throwable);
                }
            }
        });
        taskHelper.setCancelCallback(new TaskHelper.TaskCancelCallback() {
            @Override
            public void cancel() {
                taskLock.cancel();
            }
        });
        TASK_HELPERS.add(taskHelper);

        mLock.trySharedLock(new Runnable() {
            @Override
            public void run() {
                list.add(TaskUtils.createTasks(mThreadEnum, mLock));
                mThreadEnum.postTask(taskHelper);
            }
        });
        ITaskConditions<RESULT> taskConditions = new TaskConditionsImpl<>(mThreadEnum, taskLock);
        TASK_FUNCTIONS.add(taskConditions);
        return taskConditions;
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
    @NonNull
    @Override
    public <RESULT> ITaskConditions<RESULT> call(@NonNull final TaskCallable<RESPONSE, RESULT> task) {
        final TaskLock<RESULT> taskLock = new TaskLock<>();
        taskLock.lock();

        final TaskHelper<RESULT> taskHelper = new TaskHelper<>(new Task<RESULT>() {
            @Override
            public RESULT call() throws Exception {
                ITask<RESPONSE>[] iTasks = TaskUtils.createTasks(mThreadEnum, mLock);
                return task.then(iTasks);
            }
        });
        taskHelper.setTaskCallback(new TaskHelper.TaskCallback<RESULT>() {
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
        taskHelper.setCancelCallback(new TaskHelper.TaskCancelCallback() {
            @Override
            public void cancel() {
                taskLock.cancel();
            }
        });
        TASK_HELPERS.add(taskHelper);

        mLock.trySharedLock(new Runnable() {
            @Override
            public void run() {
                mThreadEnum.postTask(taskHelper);
            }
        });
        ITaskConditions<RESULT> taskConditions = new TaskConditionsImpl<>(mThreadEnum, taskLock);
        TASK_FUNCTIONS.add(taskConditions);
        return taskConditions;
    }

    /**
     * 运行任务，接收单个数据
     *
     * @param task : 任务体
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:09 PM
     */
    @NonNull
    @Override
    public <RESULT> ITaskConditions<RESULT> call(@NonNull final TaskCallableOnce<RESPONSE, RESULT> task) {
        final TaskLock<RESULT> taskLock = new TaskLock<>();
        taskLock.lock();

        final TaskHelper<RESULT> taskHelper = new TaskHelper<>(new Task<RESULT>() {
            @Override
            public RESULT call() throws Exception {
                List<TaskLock.Response<RESPONSE>> list = mLock.getResponseList();
                ITask<RESPONSE> iTask;
                if (list.size() > 0) {
                    TaskLock.Response<RESPONSE> response = list.get(0);
                    iTask = new TaskImpl<>(response.isSuccess, response.response, response.throwable, mThreadEnum, new TaskLock<RESPONSE>());
                } else {
                    iTask = new TaskImpl<>(false, null, null, mThreadEnum, new TaskLock<RESPONSE>());
                }
                return task.then(iTask);
            }
        });
        taskHelper.setTaskCallback(new TaskHelper.TaskCallback<RESULT>() {
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
        taskHelper.setCancelCallback(new TaskHelper.TaskCancelCallback() {
            @Override
            public void cancel() {
                taskLock.cancel();
            }
        });
        TASK_HELPERS.add(taskHelper);

        mLock.trySharedLock(new Runnable() {
            @Override
            public void run() {
                mThreadEnum.postTask(taskHelper);
            }
        });
        ITaskConditions<RESULT> taskConditions = new TaskConditionsImpl<>(mThreadEnum, taskLock);
        TASK_FUNCTIONS.add(taskConditions);
        return taskConditions;
    }

    /**
     * 取消当前任务
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:10 PM
     */
    @Override
    public void cancel() {
        mLock.cancel();
        for (Future<?> future : TASK_HELPERS) {
            future.cancel(true);
        }
        for (ITaskFunction<?> taskFunction : TASK_FUNCTIONS) {
            taskFunction.cancel();
        }
        TASK_HELPERS.clear();
        TASK_FUNCTIONS.clear();
    }
}
