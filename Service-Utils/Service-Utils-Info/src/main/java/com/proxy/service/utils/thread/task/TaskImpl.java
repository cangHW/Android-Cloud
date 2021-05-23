package com.proxy.service.utils.thread.task;

import com.proxy.service.api.task.ITask;
import com.proxy.service.utils.lock.TaskLock;
import com.proxy.service.utils.thread.enums.TaskThreadEnum;

/**
 * @author : cangHX
 * on 2020/08/09  4:31 PM
 */
public class TaskImpl<RESPONSE> extends TaskConditionsImpl<RESPONSE> implements ITask<RESPONSE> {

    private final boolean isSuccess;
    private final RESPONSE mResponse;
    private final Throwable mThrowable;

    public TaskImpl(boolean isSuccess, RESPONSE response, Throwable throwable, TaskThreadEnum threadEnum, TaskLock<RESPONSE> lock) {
        super(threadEnum, lock);
        this.isSuccess = isSuccess;
        this.mResponse = response;
        this.mThrowable = throwable;
    }

    /**
     * 是否执行成功
     *
     * @return true 成功，false 失败
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:22 PM
     */
    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * 获取返回值
     *
     * @return 返回值
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:10 PM
     */
    @Override
    public RESPONSE getResponse() {
        return mResponse;
    }

    /**
     * 获取异常信息
     *
     * @return 异常信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:10 PM
     */
    @Override
    public Throwable getThrowable() {
        return mThrowable;
    }

}
