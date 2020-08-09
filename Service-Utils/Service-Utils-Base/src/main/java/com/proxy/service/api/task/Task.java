package com.proxy.service.api.task;

/**
 * 任务体
 *
 * @author : cangHX
 * on 2020/07/30  6:15 PM
 */
public interface Task<RESULT> {

    /**
     * 执行体
     *
     * @return 执行后的返回值
     * @throws Exception : 执行中的异常信息
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/8/9 3:20 PM
     */
    RESULT call() throws Exception;
}
