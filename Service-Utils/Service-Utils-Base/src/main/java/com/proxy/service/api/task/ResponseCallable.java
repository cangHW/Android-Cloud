package com.proxy.service.api.task;

import java.util.concurrent.Callable;

/**
 * @author : cangHX
 * on 2020/08/09  3:57 PM
 */
public class ResponseCallable<RESPONSE, RESULT> implements Callable<RESULT> {

    private TaskCallable<RESPONSE, RESULT> mTaskCallable;
    private ITask<RESPONSE>[] mTasks;

    public ResponseCallable(TaskCallable<RESPONSE, RESULT> taskCallable, ITask<RESPONSE>[] tasks) {
        this.mTaskCallable = taskCallable;
        this.mTasks = tasks;
    }

    @Override
    public RESULT call() throws Exception {
        if (mTaskCallable != null) {
            return mTaskCallable.then(mTasks);
        }
        return null;
    }
}
