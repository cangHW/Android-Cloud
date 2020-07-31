package com.proxy.service.api.services;

import androidx.annotation.NonNull;

import com.proxy.service.api.interfaces.ITask;
import com.proxy.service.base.BaseService;

/**
 * 线程任务相关
 *
 * @author : cangHX
 * on 2020/07/30  9:01 PM
 */
public interface CloudUtilsTaskService extends BaseService {

    /**
     * 当前线程是否是主线程
     *
     * @return true 是，false 不是
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:17 PM
     */
    boolean isMainThread();

    /**
     * 发起一个 ui 线程任务
     *
     * @param runnable : 任务体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:17 PM
     */
    void postUiTask(@NonNull Runnable runnable);

    /**
     * 移除一个 ui 线程任务，如果存在
     *
     * @param runnable : 准备移除的任务体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:17 PM
     */
    void removeUiTask(@NonNull Runnable runnable);

    /**
     * 发起一个 ui 线程延迟任务
     *
     * @param runnable    : 任务体
     * @param delayMillis : 延迟时间，单位：毫秒
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:17 PM
     */
    void postUiTaskDelayed(@NonNull Runnable runnable, long delayMillis);

    /**
     * 发起一个后台线程任务
     *
     * @param runnable : 任务体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:18 PM
     */
    void postBgTask(@NonNull Runnable runnable);

    /**
     * 移除一个后台线程任务，如果存在
     *
     * @param runnable : 准备移除的任务体
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:18 PM
     */
    void removeBgTask(@NonNull Runnable runnable);

    /**
     * 发起一个后台线程延迟任务
     *
     * @param runnable    : 任务体
     * @param delayMillis : 延迟时间，单位：毫秒
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:17 PM
     */
    void postBgTaskDelayed(@NonNull Runnable runnable, long delayMillis);

    /**
     * 创建一个 ITask 对象，用于完成更复杂的线程逻辑
     *
     * @return ITask 对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/7/30 9:18 PM
     */
    @NonNull
    ITask builder();
}
