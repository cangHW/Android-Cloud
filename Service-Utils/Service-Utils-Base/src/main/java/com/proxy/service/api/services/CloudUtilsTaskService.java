package com.proxy.service.api.services;

import androidx.annotation.NonNull;

import com.proxy.service.api.task.ITaskConditions;
import com.proxy.service.api.task.ITaskFunction;
import com.proxy.service.api.task.Task;
import com.proxy.service.base.BaseService;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 线程相关
 *
 * @author : cangHX
 * on 2020/08/06  10:07 PM
 */
public interface CloudUtilsTaskService extends BaseService {

    /**
     * 是否在主线程
     *
     * @return true 是，false 不是
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:47 PM
     */
    boolean isMainThread();

    /**
     * 切换线程为主线程
     *
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:47 PM
     */
    ITaskFunction<Object> mainThread();

    /**
     * 切换线程为工作线程
     *
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:48 PM
     */
    ITaskFunction<Object> workThread();

    /**
     * 延时，工作线程执行，后续仍然为工作线程
     *
     * @param timeOut : 等待时间
     * @param unit    : 时间格式
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:48 PM
     */
    @NonNull
    ITaskConditions<Object> delay(long timeOut, @NonNull TimeUnit unit);

    /**
     * 当前置任务全部返回后继续执行，当前线程执行，后续切换到工作线程
     *
     * @param functions : 前置任务
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:50 PM
     */
    @NonNull
    ITaskConditions<Object> whenAll(@NonNull ITaskFunction<?>... functions);

    /**
     * 当前置任务全部返回，或达到最大等待时间后继续执行，当前线程执行，后续切换到工作线程
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
    ITaskConditions<Object> whenAll(long timeOut, TimeUnit unit, @NonNull ITaskFunction<?>... functions);

    /**
     * 当前置任务其中一个返回后继续执行，当前线程执行，后续切换到工作线程
     *
     * @param functions : 前置任务
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:50 PM
     */
    @NonNull
    ITaskConditions<Object> whenAny(@NonNull ITaskFunction<?>... functions);

    /**
     * 当前置任务其中一个返回，或达到最大等待时间后继续执行，当前线程执行，后续切换到工作线程
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
    ITaskConditions<Object> whenAny(long timeOut, TimeUnit unit, @NonNull ITaskFunction<?>... functions);

    /**
     * 循环执行，当前线程执行，后续切换到工作线程
     *
     * @param callable : 回调接口，返回 true 继续执行，false 跳出循环
     * @param task     : 任务体
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/6 6:53 PM
     */
    @NonNull
    <RESULT> ITaskConditions<RESULT> continueWhile(@NonNull Callable<Boolean> callable, @NonNull Task<RESULT> task);

    /**
     * 在当前线程执行，后续切换到工作线程
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
     * 在 UI 线程执行，如果不主动切换线程，后续仍然在 UI 线程
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
     * 在工作线程执行，如果不主动切换线程，后续仍然在工作线程
     *
     * @param task : 任务体
     * @return 任务对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:03 PM
     */
    @NonNull
    <RESULT> ITaskConditions<RESULT> callWorkThread(@NonNull Task<RESULT> task);

    /**
     * 取消当前任务
     *
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/7 11:10 PM
     */
    void cancel();
}
