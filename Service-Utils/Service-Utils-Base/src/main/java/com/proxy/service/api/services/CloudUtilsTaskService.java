package com.proxy.service.api.services;

import androidx.annotation.NonNull;

import com.proxy.service.api.task.ITaskConditions;
import com.proxy.service.api.task.Task;
import com.proxy.service.base.BaseService;

import java.util.concurrent.TimeUnit;

/**
 * 线程相关
 *
 * @author : cangHX
 * on 2020/08/06  10:07 PM
 */
public interface CloudUtilsTaskService extends BaseService {

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
    ITaskConditions<?> delay(long timeOut, @NonNull TimeUnit unit);

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
    <RESULT> ITaskConditions<RESULT> call(@NonNull Task<RESULT> task);

    /**
     * 在 ui 线程执行
     *
     * @param task : 任务体
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:03 PM
     */
    @NonNull
    <RESULT> ITaskConditions<RESULT> callUiThread(@NonNull Task<RESULT> task);

    /**
     * 在工作线程执行
     *
     * @param task : 任务体
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:03 PM
     */
    @NonNull
    <RESULT> ITaskConditions<RESULT> callWorkThread(@NonNull Task<RESULT> task);

}
