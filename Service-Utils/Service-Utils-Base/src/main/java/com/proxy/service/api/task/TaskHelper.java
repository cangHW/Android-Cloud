package com.proxy.service.api.task;

import com.proxy.service.api.log.Logger;

import java.util.concurrent.FutureTask;

/**
 * @author : cangHX
 * on 2020/08/09  1:41 PM
 */
public class TaskHelper<RESULT> extends FutureTask<RESULT> {

    public interface TaskCallback<RESULT> {
        /**
         * 成功回调
         *
         * @param response : 获取到的数据
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/8/9 2:22 PM
         */
        void success(RESULT response);

        /**
         * 失败回调
         *
         * @param throwable : 异常信息
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/8/9 2:24 PM
         */
        void failed(Throwable throwable);
    }

    public interface TaskCancelCallback {

        /**
         * 任务取消
         *
         * @version: 1.0
         * @author: cangHX
         * @date: 2020/8/12 9:28 PM
         */
        void cancel();

    }

    private TaskCallback<RESULT> mTaskCallback;
    private TaskCancelCallback mCancelCallback;

    public TaskHelper(Task<RESULT> task) {
        super(new ResultCallable<>(task));
    }

    /**
     * 设置回调，监听任务执行情况
     *
     * @param callback : 回调对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/10 9:16 PM
     */
    public void setTaskCallback(TaskCallback<RESULT> callback) {
        this.mTaskCallback = callback;
    }

    /**
     * 设置回调，监听任务取消情况
     *
     * @param cancelCallback : 回调对象
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/10 9:16 PM
     */
    public void setCancelCallback(TaskCancelCallback cancelCallback) {
        this.mCancelCallback = cancelCallback;
    }

    @Override
    protected void set(RESULT response) {
        super.set(response);
        if (mTaskCallback != null) {
            mTaskCallback.success(response);
        }
    }

    @Override
    protected void setException(Throwable t) {
        super.setException(t);
        Logger.Debug(t);
        if (mTaskCallback != null) {
            mTaskCallback.failed(t);
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean cancel = super.cancel(mayInterruptIfRunning);
        if (mCancelCallback != null) {
            mCancelCallback.cancel();
        }
        return cancel;
    }
}
