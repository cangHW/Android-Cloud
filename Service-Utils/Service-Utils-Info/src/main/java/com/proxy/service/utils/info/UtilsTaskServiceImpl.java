package com.proxy.service.utils.info;

import android.os.Looper;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiNewInstance;
import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.task.ITaskFunction;
import com.proxy.service.api.task.TaskHelper;
import com.proxy.service.api.task.ITaskConditions;
import com.proxy.service.api.task.Task;
import com.proxy.service.api.log.Logger;
import com.proxy.service.utils.lock.TaskLock;
import com.proxy.service.utils.thread.ThreadManager;
import com.proxy.service.utils.thread.task.TaskConditionsImpl;
import com.proxy.service.utils.thread.enums.TaskThreadEnum;
import com.proxy.service.utils.thread.task.TaskFunctionImpl;
import com.proxy.service.utils.util.TaskUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author : cangHX
 * on 2020/08/07  11:24 PM
 */
@CloudApiNewInstance
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_TASK)
public class UtilsTaskServiceImpl implements CloudUtilsTaskService {

    private final List<Future<?>> TASK_HELPERS = new ArrayList<>();
    private final List<ITaskFunction<?>> TASK_FUNCTIONS = new ArrayList<>();

    /**
     * 是否在主线程
     *
     * @return true 是，false 不是
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:47 PM
     */
    @Override
    public boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
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
    public ITaskFunction<Object> mainThread() {
        ITaskConditions<Object> taskConditions = new TaskConditionsImpl<>(TaskThreadEnum.MAIN, new TaskLock<>());
        TASK_FUNCTIONS.add(taskConditions);
        return taskConditions.mainThread();
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
    public ITaskFunction<Object> workThread() {
        ITaskConditions<Object> taskConditions = new TaskConditionsImpl<>(TaskThreadEnum.WORK, new TaskLock<>());
        TASK_FUNCTIONS.add(taskConditions);
        return taskConditions.workThread();
    }

    /**
     * 延时
     *
     * @param timeOut : 等待时间
     * @param unit    : 时间格式
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:48 PM
     */
    @NonNull
    @Override
    public ITaskConditions<Object> delay(long timeOut, @NonNull TimeUnit unit) {
        final TaskLock<Object> taskLock = new TaskLock<>();
        taskLock.lock();

        TaskHelper<Object> taskHelper = new TaskHelper<>(new Task<Object>() {
            @Override
            public Object call() {
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

        ThreadManager.postScheduled(taskHelper, timeOut, unit);

        ITaskConditions<Object> taskConditions = new TaskConditionsImpl<>(TaskThreadEnum.CURRENT, taskLock);
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
    public ITaskConditions<Object> whenAll(@NonNull ITaskFunction<?>... functions) {
        ITaskFunction<Object> taskFunction = new TaskFunctionImpl<>(TaskThreadEnum.CURRENT, new TaskLock<>());
        ITaskConditions<Object> taskConditions = taskFunction.whenAll(functions);
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
        ITaskFunction<Object> taskFunction = new TaskFunctionImpl<>(TaskThreadEnum.CURRENT, new TaskLock<>());
        ITaskConditions<Object> taskConditions = taskFunction.whenAll(timeOut, unit, functions);
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
        ITaskFunction<Object> taskFunction = new TaskFunctionImpl<>(TaskThreadEnum.CURRENT, new TaskLock<>());
        ITaskConditions<Object> taskConditions = taskFunction.whenAny(functions);
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
        ITaskFunction<Object> taskFunction = new TaskFunctionImpl<>(TaskThreadEnum.CURRENT, new TaskLock<>());
        ITaskConditions<Object> taskConditions = taskFunction.whenAny(timeOut, unit, functions);
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
    public <RESULT> ITaskConditions<RESULT> continueWhile(@NonNull final Callable<Boolean> callable, @NonNull final Task<RESULT> task) {
        final TaskLock<RESULT> taskLock = new TaskLock<>();
        taskLock.lock();

        final TaskHelper<RESULT> taskHelper = new TaskHelper<>(new Task<RESULT>() {
            @Override
            public RESULT call() throws Exception {
                while (callable.call()) {
                    RESULT result = task.call();
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

        TaskThreadEnum.CURRENT.postTask(taskHelper);

        ITaskConditions<RESULT> taskConditions = new TaskConditionsImpl<>(TaskThreadEnum.CURRENT, taskLock);
        TASK_FUNCTIONS.add(taskConditions);
        return taskConditions;
    }

    /**
     * 在当前线程执行
     *
     * @param task : 任务体
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:02 PM
     */
    @NonNull
    @Override
    public <RESULT> ITaskConditions<RESULT> call(@NonNull Task<RESULT> task) {
        return call(task, TaskThreadEnum.CURRENT);
    }

    /**
     * 在前台线程执行
     *
     * @param task : 任务体
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:03 PM
     */
    @NonNull
    @Override
    public <RESULT> ITaskConditions<RESULT> callUiThread(@NonNull Task<RESULT> task) {
        return call(task, TaskThreadEnum.MAIN);
    }

    /**
     * 在后台线程执行
     *
     * @param task : 任务体
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:03 PM
     */
    @NonNull
    @Override
    public <RESULT> ITaskConditions<RESULT> callWorkThread(@NonNull Task<RESULT> task) {
        return call(task, TaskThreadEnum.WORK);
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
        for (Future<?> future : TASK_HELPERS) {
            future.cancel(true);
        }
        for (ITaskFunction<?> taskFunction : TASK_FUNCTIONS) {
            taskFunction.cancel();
        }
        TASK_HELPERS.clear();
        TASK_FUNCTIONS.clear();
    }

    /**
     * 执行任务
     */
    private <RESULT> ITaskConditions<RESULT> call(Task<RESULT> task, TaskThreadEnum threadEnum) {
        final TaskLock<RESULT> taskLock = new TaskLock<>();
        taskLock.lock();
        TaskHelper<RESULT> taskHelper = TaskUtils.createTaskHelper(task);
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

        threadEnum.postTask(taskHelper);

        ITaskConditions<RESULT> taskConditions = new TaskConditionsImpl<>(threadEnum, taskLock);
        TASK_FUNCTIONS.add(taskConditions);
        return taskConditions;
    }
}
