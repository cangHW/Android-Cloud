package com.proxy.service.api.task;

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

    private TaskCallback<RESULT> mCallback;

    public TaskHelper(Task<RESULT> task) {
        super(new ResultCallable<>(task));
    }

    public <RESPONSE> TaskHelper(TaskCallable<RESPONSE, RESULT> task, ITask<RESPONSE>[] iTasks) {
        super(new ResponseCallable<>(task, iTasks));
    }

    public void setCallback(TaskCallback<RESULT> callback) {
        this.mCallback = callback;
    }

    @Override
    protected void set(RESULT response) {
        super.set(response);
        if (mCallback != null) {
            mCallback.success(response);
        }
    }

    @Override
    protected void setException(Throwable t) {
        super.setException(t);
        if (mCallback != null) {
            mCallback.failed(t);
        }
    }
}
