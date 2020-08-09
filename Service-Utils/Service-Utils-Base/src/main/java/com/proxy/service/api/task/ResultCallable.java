package com.proxy.service.api.task;

import java.util.concurrent.Callable;

/**
 * @author : cangHX
 * on 2020/08/09  4:06 PM
 */
public class ResultCallable<RESULT> implements Callable<RESULT> {

    private Task<RESULT> mTask;

    ResultCallable(Task<RESULT> task) {
        this.mTask = task;
    }

    @Override
    public RESULT call() throws Exception {
        if (mTask != null) {
            return mTask.call();
        }
        return null;
    }
}
