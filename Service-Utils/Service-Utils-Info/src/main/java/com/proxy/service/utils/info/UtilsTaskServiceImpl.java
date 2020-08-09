package com.proxy.service.utils.info;

import androidx.annotation.NonNull;

import com.proxy.service.annotations.CloudApiNewInstance;
import com.proxy.service.annotations.CloudApiService;
import com.proxy.service.api.services.CloudUtilsTaskService;
import com.proxy.service.api.tag.CloudServiceTagUtils;
import com.proxy.service.api.task.TaskHelper;
import com.proxy.service.api.task.ITaskConditions;
import com.proxy.service.api.task.Task;
import com.proxy.service.utils.lock.TaskLock;
import com.proxy.service.utils.pool.thread.task.TaskConditionsImpl;
import com.proxy.service.utils.pool.thread.enums.TaskThreadEnum;
import com.proxy.service.utils.util.TaskUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author : cangHX
 * on 2020/08/07  11:24 PM
 */
@CloudApiNewInstance
@CloudApiService(serviceTag = CloudServiceTagUtils.UTILS_TASK)
public class UtilsTaskServiceImpl implements CloudUtilsTaskService {

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
    public ITaskConditions<?> delay(long timeOut, @NonNull TimeUnit unit) {
        TaskLock<?> taskLock = new TaskLock<>();
        taskLock.lock();
//        //todo 添加延迟，解锁
        taskLock.unLock();
        return new TaskConditionsImpl<>(TaskThreadEnum.CURRENT, taskLock);
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
     * 执行任务
     */
    private <RESULT> ITaskConditions<RESULT> call(Task<RESULT> task, TaskThreadEnum threadEnum) {
        final TaskLock<RESULT> taskLock = new TaskLock<>();
        taskLock.lock();
        TaskHelper<RESULT> taskHelper = TaskUtils.createTaskHelper(task);
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
        threadEnum.postTask(taskHelper);
        return new TaskConditionsImpl<>(threadEnum, taskLock);
    }
}
