package com.proxy.service.utils.pool.thread.enums;

import com.proxy.service.api.task.TaskHelper;
import com.proxy.service.utils.pool.thread.ThreadManager;

import java.util.concurrent.Future;

/**
 * @author : cangHX
 * on 2020/08/07  5:15 PM
 */
public enum TaskThreadEnum {

    /**
     * 主线程
     */
    MAIN,

    /**
     * 工作线程
     */
    WORK,

    /**
     * 当前线程
     */
    CURRENT;

    public <RESULT> void postTask(TaskHelper<RESULT> taskHelper) {
        switch (this) {
            case MAIN:
                ThreadManager.postMain(taskHelper);
                break;
            case WORK:
                ThreadManager.postWork(taskHelper);
                break;
            case CURRENT:
                ThreadManager.postCurrent(taskHelper);
                break;
            default:
        }
    }
}
